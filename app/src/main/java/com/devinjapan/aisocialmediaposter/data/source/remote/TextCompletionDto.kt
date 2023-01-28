package com.devinjapan.aisocialmediaposter.data.source.remote

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class TextCompletionDto(
    val created: Int = 0,
    val usage: Usage,
    val model: String = "",
    val id: String = "",
    val choices: List<ChoicesItem>?
) {
    @kotlinx.serialization.Serializable
    data class Usage(
        @SerialName("completion_tokens")
        val completionTokens: Int = 0,
        @SerialName("prompt_tokens")
        val promptTokens: Int = 0,
        @SerialName("total_tokens")
        val totalTokens: Int = 0
    )

    @kotlinx.serialization.Serializable
    data class ChoicesItem(
        val index: Int = 0,
        val text: String = ""
    )
}
