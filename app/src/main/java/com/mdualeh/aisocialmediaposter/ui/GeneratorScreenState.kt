package com.mdualeh.aisocialmediaposter.ui

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import com.mdualeh.aisocialmediaposter.domain.model.SocialMedia
import com.mdualeh.aisocialmediaposter.domain.model.TextCompletion
import com.mdualeh.aisocialmediaposter.ui.model.SocialMediaItem

data class GeneratorScreenState(
    val image: Bitmap? = null,
    val textCompletion: TextCompletion? = null,
    var modifiedText: String? = null,
    val loadedTags: MutableList<String> = mutableStateListOf(),
    val selectedSocialMediaItem: SocialMediaItem? = null,
    val selectedSocialMedia: SocialMedia = SocialMedia.INSTAGRAM,
    val isLoading: Boolean = false,
    val isLoadingTags: Boolean = false,
    val error: String? = null
)
