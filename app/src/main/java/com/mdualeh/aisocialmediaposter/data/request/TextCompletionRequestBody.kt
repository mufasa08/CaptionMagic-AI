package com.mdualeh.aisocialmediaposter.data.request

data class TextCompletionRequestBody(
    val maxTokens: Int,
    val temperature: Double = 0.9,
    val model: String = "text-davinci-003",
    val prompt: String,
)
