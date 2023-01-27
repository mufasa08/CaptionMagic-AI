package com.devinjapan.shared.data.source.remote

import com.example.shared.data.request.TextCompletionRequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAIApi {
    // https://beta.openai.com/docs/api-reference/completions/create
    @POST("completions")
    suspend fun postTextCompletionReply(
        @Body textCompletionRequestBody: TextCompletionRequestBody
    ): TextCompletionDto
}
