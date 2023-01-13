package com.mdualeh.aisocialmediaposter.ui

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import com.mdualeh.aisocialmediaposter.domain.weather.TextCompletion

data class GeneratorScreenState(
    val image: Bitmap? = null,
    val textCompletion: TextCompletion? = null,
    val loadedTags: MutableList<String> = mutableStateListOf(),
    val isLoading: Boolean = false,
    val error: String? = null
)
