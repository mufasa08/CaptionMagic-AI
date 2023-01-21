package com.devinjapan.aisocialmediaposter.data.utils

import com.devinjapan.aisocialmediaposter.domain.model.SocialMedia

// todo add support for multiple languages
fun SocialMedia.toChatGPTUnderstandableString(
    selectedTone: String?,
    keywords: List<String>
): String {
    return when (this) {
        SocialMedia.INSTAGRAM -> {
            "Create a ${selectedTone ?: "cool"} instagram post with the following keywords: ${keywords.joinToString()}}"
        }
        SocialMedia.TWITTER -> {
            "Create a ${selectedTone ?: "catchy"} twitter tweet with the following keywords: ${keywords.joinToString()}}"
        }
        SocialMedia.OTHER -> {
            "Create a ${selectedTone ?: "catchy"} social media post with the following keywords: ${keywords.joinToString()}}"
        }
    }
}
