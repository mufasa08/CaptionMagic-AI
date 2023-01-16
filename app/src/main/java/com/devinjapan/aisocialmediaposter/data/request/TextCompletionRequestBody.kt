package com.devinjapan.aisocialmediaposter.data.request

import com.squareup.moshi.Json

data class TextCompletionRequestBody(
    val model: String = "text-davinci-003",
    val prompt: String,
    @field:Json(name = "max_tokens")
    val maxTokens: Int,
    val temperature: Double = 0.9,
)
