package com.devinjapan.aisocialmediaposter.ui.state

data class SettingsState(
    val selectedCaptionTone: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
