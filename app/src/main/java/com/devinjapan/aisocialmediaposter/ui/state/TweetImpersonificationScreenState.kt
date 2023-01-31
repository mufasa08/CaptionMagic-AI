package com.devinjapan.aisocialmediaposter.ui.state

import androidx.compose.runtime.mutableStateListOf
import com.devinjapan.aisocialmediaposter.shared.domain.model.TextCompletion

data class TweetImpersonificationScreenState(
    val image: String? = null,
    val textCompletion: TextCompletion? = null,
    var modifiedText: String? = null,
    val loadedTags: MutableList<String> = mutableStateListOf(),
    val recentList: MutableList<String> = mutableStateListOf(),
    val selectedPersonality: String = "",
    val hidePromoHashtags: Boolean = false,
    val launchNumber: Long = 0L,
    val isFirstLaunch: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingFirstLaunchCheck: Boolean = true,
    val keywordError: ValidationError = ValidationError.NONE,
    val isLoadingTags: Boolean = false,
    val error: ErrorInfo? = null
) {
    data class ErrorInfo(val errorMessage: String, val exception: Exception? = null)
    enum class ValidationError {
        TOO_LONG,
        TOO_MANY_KEYWORDS,
        NONE,
    }
}
