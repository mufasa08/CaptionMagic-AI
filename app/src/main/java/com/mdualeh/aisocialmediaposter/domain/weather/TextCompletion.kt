package com.mdualeh.aisocialmediaposter.domain.weather

data class TextCompletion(
    val choices: List<ChoicesItem>?,
) {
    data class ChoicesItem(
        val index: Int = 0,
        val text: String = "",
    )
}


