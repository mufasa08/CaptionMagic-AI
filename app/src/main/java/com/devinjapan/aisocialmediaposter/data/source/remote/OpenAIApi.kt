package com.devinjapan.aisocialmediaposter.data.source.remote

import com.devinjapan.aisocialmediaposter.data.request.TextCompletionRequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAIApi {
    // https://beta.openai.com/docs/api-reference/completions/create
    @POST("completions")
    suspend fun postTextCompletionReply(
        @Body textCompletionRequestBody: TextCompletionRequestBody
    ): TextCompletionDto
}
