package com.devinjapan.aisocialmediaposter.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devinjapan.aisocialmediaposter.shared.domain.repository.DataStoreRepository
import com.devinjapan.aisocialmediaposter.shared.domain.util.HIDE_PROMO_HASHTAGS
import com.devinjapan.aisocialmediaposter.shared.domain.util.SELECTED_TONE
import com.devinjapan.aisocialmediaposter.ui.state.SettingsState
import com.devinjapan.aisocialmediaposter.ui.utils.LAUNCH_COUNT
import com.devinjapan.aisocialmediaposter.ui.utils.RECENT_KEYWORD_LIST
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val analyticsTracker: com.devinjapan.aisocialmediaposter.shared.analytics.AnalyticsTracker
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
            val selectedTone = dataStoreRepository.getString(SELECTED_TONE)
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
            val hidePromoHashtags = dataStoreRepository.getBoolean(HIDE_PROMO_HASHTAGS) ?: false
            state = state.copy(
                hidePromoHashtags = hidePromoHashtags,
                isLoading = false
            )
        }
    }

    fun clearRecentList() {
        analyticsTracker.logClearedRecentList()
        viewModelScope.launch {
            dataStoreRepository.clearPreferences(RECENT_KEYWORD_LIST)
        }
    }

    fun updateSelectedTone(selectedTone: String) {
        analyticsTracker.logUpdateSelectedTone(selectedTone)
        state = state.copy(
            selectedCaptionTone = selectedTone,
            isLoading = true
        )
        viewModelScope.launch {
            state.selectedCaptionTone?.let { dataStoreRepository.putString(SELECTED_TONE, it) }
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
                dataStoreRepository.putBoolean(
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

    fun resetWalkthrough() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )
            dataStoreRepository.putLong(
                LAUNCH_COUNT,
                1L
            )

            state = state.copy(
                isLoading = false
            )
        }
    }

    fun openSourceLicensesClicked() {
        analyticsTracker.logEvent("oss_licenses_viewed", null)
    }

    fun openPrivacyPolicyClicked() {
        analyticsTracker.logEvent("privacy_policy_viewed", null)
    }
}
