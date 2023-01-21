package com.devinjapan.aisocialmediaposter.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devinjapan.aisocialmediaposter.data.repository.DataStoreRepositoryImpl
import com.devinjapan.aisocialmediaposter.ui.state.SettingsState
import com.devinjapan.aisocialmediaposter.ui.utils.DATASTORE_IS_FIRST_LAUNCH
import com.devinjapan.aisocialmediaposter.ui.utils.DATASTORE_RECENT_KEYWORD_LIST
import com.devinjapan.aisocialmediaposter.ui.utils.SELECTED_TONE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreRepositoryImpl: DataStoreRepositoryImpl
) : ViewModel() {

    var state by mutableStateOf(SettingsState())
        private set

    init {
        getSelectedTone()
    }

    private fun getSelectedTone() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )
            val selectedTone = dataStoreRepositoryImpl.getString(SELECTED_TONE)
            state = state.copy(
                selectedCaptionTone = selectedTone,
                isLoading = false
            )
        }
    }

    fun clearRecentList() {
        viewModelScope.launch {
            dataStoreRepositoryImpl.clearPreferences(DATASTORE_RECENT_KEYWORD_LIST)
        }
    }

    fun updateSelectedTone(selectedTone: String) {
        viewModelScope.launch {
            state = state.copy(
                selectedCaptionTone = selectedTone,
                isLoading = true
            )
            state.selectedCaptionTone?.let { dataStoreRepositoryImpl.putString(SELECTED_TONE, it) }
            state = state.copy(
                isLoading = false
            )
        }
    }

    fun resetFirstLaunchRelay() {
        clearRecentList()
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )
            dataStoreRepositoryImpl.putBoolean(DATASTORE_IS_FIRST_LAUNCH, true)
            state = state.copy(
                isLoading = false
            )
        }
    }
}
