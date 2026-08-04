package es.jshcd.android.rssreader.ui.viewmodel

import android.util.Log
import android.webkit.URLUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.jshcd.android.rssreader.ui.state.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    fun addDataSource(rssURL: String) {
        if (URLUtil.isValidUrl(rssURL)) {
            if (!_uiState.value.dataSources.contains(rssURL)) {
                _uiState.update { it.copy(dataSources = it.dataSources + rssURL) }
            }
            return
        }
        Log.w(TAG, "Given URL is not valid: $rssURL")
    }

    fun removeDataSource(rssURL: String) {
        _uiState.update { it.copy(dataSources = it.dataSources - rssURL) }
    }

    companion object {
        const val TAG = "SettingsViewModel"
    }
}
