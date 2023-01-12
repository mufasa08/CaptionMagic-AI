package com.mdualeh.aisocialmediaposter.ui

import com.mdualeh.aisocialmediaposter.domain.weather.TextCompletion

data class GeneratorScreenState(
    val textCompletion: TextCompletion? = null,
    val loadedTags: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
