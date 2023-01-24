package com.devinjapan.aisocialmediaposter.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devinjapan.aisocialmediaposter.analytics.AnalyticsTracker
import com.devinjapan.aisocialmediaposter.data.repository.DataStoreRepositoryImpl
import com.devinjapan.aisocialmediaposter.ui.state.SettingsState
import com.devinjapan.aisocialmediaposter.ui.utils.HIDE_PROMO_HASHTAGS
import com.devinjapan.aisocialmediaposter.ui.utils.RECENT_KEYWORD_LIST
import com.devinjapan.aisocialmediaposter.ui.utils.SELECTED_TONE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreRepositoryImpl: DataStoreRepositoryImpl,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    var state by mutableStateOf(SettingsState())
        private set

    init {
        getSelectedTone()
        getHidePromoHashtagState()
    }

    private fun getSelectedTone() {
        state = state.copy(
            isLoading = true
        )
        viewModelScope.launch {
            val selectedTone = dataStoreRepositoryImpl.getString(SELECTED_TONE)
            state = state.copy(
                selectedCaptionTone = selectedTone,
                isLoading = false
            )
        }
    }

    private fun getHidePromoHashtagState() {
        state = state.copy(
            isLoading = true
        )
        viewModelScope.launch {
            val hidePromoHashtags = dataStoreRepositoryImpl.getBoolean(HIDE_PROMO_HASHTAGS) ?: false
            state = state.copy(
                hidePromoHashtags = hidePromoHashtags,
                isLoading = false
            )
        }
    }

    fun clearRecentList() {
        analyticsTracker.logClearedRecentList()
        viewModelScope.launch {
            dataStoreRepositoryImpl.clearPreferences(RECENT_KEYWORD_LIST)
        }
    }

    fun updateSelectedTone(selectedTone: String) {
        analyticsTracker.logUpdateSelectedTone(selectedTone)
        state = state.copy(
            selectedCaptionTone = selectedTone,
            isLoading = true
        )
        viewModelScope.launch {
            state.selectedCaptionTone?.let { dataStoreRepositoryImpl.putString(SELECTED_TONE, it) }
            state = state.copy(
                isLoading = false
            )
        }
    }

    fun onBugReportLinkClicked() {
        analyticsTracker.logLinkClicked("bug_report")
    }

    fun onSubmitFeedbackClicked() {
        analyticsTracker.logLinkClicked("submit_feedback")
    }

    fun toggleHidePromoHashtags() {
        analyticsTracker.logEvent("hide_promo_Tags", null)
        val newSetting: Boolean = !state.hidePromoHashtags

        viewModelScope.launch {
            state.selectedCaptionTone?.let {
                dataStoreRepositoryImpl.putBoolean(
                    HIDE_PROMO_HASHTAGS,
                    newSetting
                )
            }
            state = state.copy(
                hidePromoHashtags = newSetting,
                isLoading = false
            )
        }
    }
}
