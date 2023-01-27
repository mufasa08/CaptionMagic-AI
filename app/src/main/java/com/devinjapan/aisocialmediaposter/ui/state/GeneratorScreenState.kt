package com.devinjapan.aisocialmediaposter.ui.state

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import com.example.shared.domain.model.SocialMedia
import com.example.shared.domain.model.TextCompletion

data class GeneratorScreenState(
    val image: Bitmap? = null,
    val textCompletion: com.example.shared.domain.model.TextCompletion? = null,
    var modifiedText: String? = null,
    val loadedTags: MutableList<String> = mutableStateListOf(),
    val selectedSocialMedia: com.example.shared.domain.model.SocialMedia = com.example.shared.domain.model.SocialMedia.OTHER,
    val recentList: MutableList<String> = mutableStateListOf(),
    val selectedCaptionTone: String? = null,
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
