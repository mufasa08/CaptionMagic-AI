package com.devinjapan.aisocialmediaposter.ui.state

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import com.devinjapan.aisocialmediaposter.domain.model.SocialMedia
import com.devinjapan.aisocialmediaposter.domain.model.TextCompletion

data class GeneratorScreenState(
    val image: Bitmap? = null,
    val textCompletion: TextCompletion? = null,
    var modifiedText: String? = null,
    val loadedTags: MutableList<String> = mutableStateListOf(),
    val selectedSocialMedia: SocialMedia = SocialMedia.OTHER,
    val recentList: MutableList<String> = mutableStateListOf(),
    val selectedCaptionTone: String? = null,
    val isLoading: Boolean = false,
    val isLoadingTags: Boolean = false,
    val error: ErrorInfo? = null
) {
    data class ErrorInfo(val errorMessage: String, val exception: Exception? = null)
}
