package com.mdualeh.aisocialmediaposter.domain.model

enum class SocialMedia(val maxTokenLimit: Int) {
    INSTAGRAM(2200),
    TWITTER(280),
    OTHER(2048),
}
