package es.jshcd.android.rssreader.ui.viewmodel

import android.util.Log
import android.webkit.URLUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.jshcd.android.rssreader.ui.state.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    fun updateSetting(rssURL: String) {
        if (rssURL.isEmpty()) {
            viewModelScope.launch {
                _uiState.emit(SettingsState())
            }
            return
        }

        if (URLUtil.isValidUrl(rssURL)) {
            viewModelScope.launch {
                _uiState.emit(uiState.value.copy(dataSource = rssURL))
            }
            return
        }

        Log.w(TAG, "Given URL is not valid: $rssURL")
    }

    companion object {
        const val TAG = "SettingsViewModel"
    }
}
