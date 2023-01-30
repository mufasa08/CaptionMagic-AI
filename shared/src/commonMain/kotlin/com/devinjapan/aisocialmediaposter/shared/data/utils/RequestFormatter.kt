package com.devinjapan.aisocialmediaposter.shared.data.utils

import com.devinjapan.aisocialmediaposter.shared.domain.model.SocialMedia

// todo add support for multiple languages
fun SocialMedia.toChatGPTUnderstandableString(
    selectedTone: String?,
    keywords: List<String>
): String {
    return when (this) {
        SocialMedia.INSTAGRAM -> {
            "${selectedTone ?: "cool"} instagram post with keywords: ${keywords.joinToString()}}"
        }
        SocialMedia.TWITTER -> {
            "${selectedTone ?: "catchy"} twitter tweet with keywords: ${keywords.joinToString()}}"
        }
        SocialMedia.OTHER -> {
            "${selectedTone ?: "catchy"} social media post following keywords: ${keywords.joinToString()}}"
        }
    }
}
