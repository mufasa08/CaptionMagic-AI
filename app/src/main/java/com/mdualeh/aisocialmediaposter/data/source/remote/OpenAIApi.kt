package com.plcoding.weatherapp.data.remote

import com.mdualeh.aisocialmediaposter.data.request.TextCompletionRequestBody
import com.mdualeh.aisocialmediaposter.data.source.remote.TextCompletionDto
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAIApi {
    // https://beta.openai.com/docs/api-reference/completions/create
    @POST("completions")
    suspend fun postTextCompletionReply(
        @Body textCompletionRequestBody: TextCompletionRequestBody,
    ): TextCompletionDto
}
