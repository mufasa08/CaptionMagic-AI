package com.plcoding.weatherapp.data.remote

import com.mdualeh.aisocialmediaposter.data.remote.TextCompletionDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenAIApi {
    // https://beta.openai.com/docs/api-reference/completions/create
    @GET("/v1/completions/")
    suspend fun getTextCompletionReply(
        @Query("model") model: String = "text-davinci-003",
        @Query("prompt") keywords: String,
        @Query("max_tokens") maxWords: Int,
        @Query("temperature") temperature: Double = 0.9,
    ): TextCompletionDto
}