package com.devinjapan.shared.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TextCompletionDto(
    val created: Int = 0,
    val usage: Usage,
    val model: String = "",
    val id: String = "",
    val choices: List<ChoicesItem>?
) {
    @Serializable
    data class Usage(
        @SerialName("completion_tokens")
        val completionTokens: Int = 0,
        @SerialName("prompt_tokens")
        val promptTokens: Int = 0,
        @SerialName("total_tokens")
        val totalTokens: Int = 0
    )

    @Serializable
    data class ChoicesItem(
        val index: Int = 0,
        val text: String = ""
    )
}
