package es.jshcd.android.rssreader.ui.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.webkit.URLUtil
import androidx.lifecycle.AndroidViewModel
import es.jshcd.android.rssreader.ui.state.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("rss_reader_prefs", Context.MODE_PRIVATE)
    
    private val _uiState = MutableStateFlow(SettingsState(loadDataSources()))
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    private fun loadDataSources(): List<String> {
        val savedString = sharedPreferences.getString(KEY_DATA_SOURCES, null)
        return if (savedString.isNullOrBlank()) {
            listOf("https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada")
        } else {
            savedString.split("|")
        }
    }

    private fun saveDataSources(sources: List<String>) {
        sharedPreferences.edit().putString(KEY_DATA_SOURCES, sources.joinToString("|")).apply()
    }

    fun addDataSource(rssURL: String) {
        if (URLUtil.isValidUrl(rssURL)) {
            if (!_uiState.value.dataSources.contains(rssURL)) {
                _uiState.update { state ->
                    val newList = state.dataSources + rssURL
                    saveDataSources(newList)
                    state.copy(dataSources = newList)
                }
            }
            return
        }
        Log.w(TAG, "Given URL is not valid: $rssURL")
    }

    fun removeDataSource(rssURL: String) {
        _uiState.update { state ->
            val newList = state.dataSources - rssURL
            saveDataSources(newList)
            state.copy(dataSources = newList)
        }
    }

    companion object {
        const val TAG = "SettingsViewModel"
        const val KEY_DATA_SOURCES = "data_sources"
    }
}
