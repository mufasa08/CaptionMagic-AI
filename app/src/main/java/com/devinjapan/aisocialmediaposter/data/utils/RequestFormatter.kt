package com.devinjapan.aisocialmediaposter.data.utils

import com.devinjapan.aisocialmediaposter.domain.model.SocialMedia

// todo add support for multiple languages
fun SocialMedia.toChatGPTUnderstandableString(keywords: List<String>): String {
    return when (this) {
        SocialMedia.INSTAGRAM -> {
            "Create a cool instagram post with the following keywords: ${keywords.joinToString()}}"
        }
        SocialMedia.TWITTER -> {
            "Create a catchy twitter tweet with the following keywords: ${keywords.joinToString()}}"
        }
        SocialMedia.OTHER -> {
            "Create a catchy social media post with the following keywords: ${keywords.joinToString()}}"
        }
    }
}
