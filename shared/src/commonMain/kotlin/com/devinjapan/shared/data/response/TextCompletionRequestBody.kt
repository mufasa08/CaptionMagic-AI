package com.devinjapan.shared.data.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class TextCompletionRequestBody(
    val model: String = "text-davinci-003",
    val prompt: String,
    @SerialName("max_tokens")
    val maxTokens: Int,
    val temperature: Double = 0.9,
    val user: String
)
