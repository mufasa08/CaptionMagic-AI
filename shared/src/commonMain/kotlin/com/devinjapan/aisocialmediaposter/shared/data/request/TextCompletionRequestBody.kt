package com.devinjapan.aisocialmediaposter.shared.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TextCompletionRequestBody(
    val model: String = "text-davinci-003",
    val prompt: String,
    @SerialName("max_tokens")
    val maxTokens: Int,
    val temperature: Double = 0.9,
    val user: String
)
