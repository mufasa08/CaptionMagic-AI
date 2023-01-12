package com.mdualeh.aisocialmediaposter.ui

import android.graphics.Bitmap
import com.mdualeh.aisocialmediaposter.domain.weather.TextCompletion

data class GeneratorScreenState(
    val image: Bitmap? = null,
    val textCompletion: TextCompletion? = null,
    val loadedTags: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
