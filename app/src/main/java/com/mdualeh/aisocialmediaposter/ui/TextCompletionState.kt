package com.mdualeh.aisocialmediaposter.ui

import com.mdualeh.aisocialmediaposter.domain.weather.TextCompletion

data class TextCompletionState(
    val textCompletion: TextCompletion? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
