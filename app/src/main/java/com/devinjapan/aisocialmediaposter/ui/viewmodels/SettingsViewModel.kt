package com.devinjapan.aisocialmediaposter.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devinjapan.aisocialmediaposter.data.repository.DataStoreRepositoryImpl
import com.devinjapan.aisocialmediaposter.ui.state.SettingsState
import com.devinjapan.aisocialmediaposter.ui.utils.RECENT_KEYWORD_LIST
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
    }

    private fun getRecentList() {
        state = state.copy(
            isLoading = true,
            error = null
        )
        viewModelScope.launch {
            val result = dataStoreRepositoryImpl.getList(RECENT_KEYWORD_LIST)
            // state.recentList.addAll(result)
            state = state.copy(
                isLoading = false
            )
        }
    }

    private fun saveRecentList() {
        viewModelScope.launch {
            // dataStoreRepositoryImpl.putList(RECENT_KEYWORD_LIST, state.recentList)
        }
    }
}
