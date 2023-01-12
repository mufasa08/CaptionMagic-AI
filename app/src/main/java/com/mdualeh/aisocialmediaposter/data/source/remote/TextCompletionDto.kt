package com.mdualeh.aisocialmediaposter.data.source.remote

data class TextCompletionDto(
    val created: Int = 0,
    val usage: Usage,
    val model: String = "",
    val id: String = "",
    val choices: List<ChoicesItem>?,
) {
    data class Usage(
        val completionTokens: Int = 0,
        val promptTokens: Int = 0,
        val totalTokens: Int = 0
    )
    data class ChoicesItem(
        val index: Int = 0,
        val text: String = "",
    )
}
