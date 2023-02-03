package com.devinjapan.aisocialmediaposter.shared.data.repository

import com.devinjapan.aisocialmediaposter.BuildKonfig
import com.devinjapan.aisocialmediaposter.shared.data.error.toApiException
import com.devinjapan.aisocialmediaposter.shared.data.mappers.toTextCompletion
import com.devinjapan.aisocialmediaposter.shared.data.request.TextCompletionRequestBody
import com.devinjapan.aisocialmediaposter.shared.data.response.TextCompletionDto
import com.devinjapan.aisocialmediaposter.shared.data.utils.MAX_NUMBER_OF_TOKENS_CHAT_GPT
import com.devinjapan.aisocialmediaposter.shared.data.utils.toChatGPTUnderstandableString
import com.devinjapan.aisocialmediaposter.shared.domain.model.SocialMedia
import com.devinjapan.aisocialmediaposter.shared.domain.model.TextCompletion
import com.devinjapan.aisocialmediaposter.shared.domain.repository.AuthRepository
import com.devinjapan.aisocialmediaposter.shared.domain.repository.DataStoreRepository
import com.devinjapan.aisocialmediaposter.shared.domain.repository.TextCompletionRepository
import com.devinjapan.aisocialmediaposter.shared.domain.util.HIDE_PROMO_HASHTAGS
import com.devinjapan.aisocialmediaposter.shared.domain.util.Resource
import com.devinjapan.aisocialmediaposter.shared.domain.util.SELECTED_TONE
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.reflect.*

class TextCompletionRepositoryImpl(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val client: HttpClient,
    private val analyticsTracker: com.devinjapan.aisocialmediaposter.shared.analytics.AnalyticsTracker
) : TextCompletionRepository {

    override suspend fun getReplyFromTextCompletionAPI(
        keywords: List<String>,
        maxWords: Int,
        type: SocialMedia
    ): Resource<TextCompletion> {
        return try {
            val selectedTone = dataStoreRepository.getString(SELECTED_TONE)
            val user = authRepository.getSignedInUserIfExists()
            val hideHashTags = dataStoreRepository.getBoolean(HIDE_PROMO_HASHTAGS) ?: false
            val prompt =
                type.toChatGPTUnderstandableString(selectedTone, keywords).cleanPromptString()
            val body = TextCompletionRequestBody(
                // fix magic number
                maxTokens = minOf(maxWords, MAX_NUMBER_OF_TOKENS_CHAT_GPT),
                prompt = prompt,
                user = user?.userId ?: "not-signed-in"
            )
            val apiKey = BuildKonfig.OpenApiSecret
            val responseData = client.post("https://api.openai.com/v1/completions") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(apiKey)
                }
                setBody(body, typeInfo<TextCompletionRequestBody>())
            }

            val data = responseData.body<TextCompletionDto>().toTextCompletion(hideHashTags)
            val resource = Resource.Success(
                data = data
            )
            analyticsTracker.logApiCall("getReplyFromTextCompletionAPI", type.name)
            resource
        } catch (e: Exception) {
            e.printStackTrace()
            val apiException = e.toApiException()

            analyticsTracker.logApiCallError(
                "getReplyFromTextCompletionAPI",
                type.name,
                apiException.type.name,
                null
            )
            Resource.Error(message = e.message ?: "Something went wrong.", exception = apiException)
        }
    }
}

private fun String.cleanPromptString(): String {
    val newString = this.apply {
        replace("(\\r|\\n|\\r\\n)+", "")
        replace("(\\r\\n)+", "")
        replace("(\\n)+", "")
    }.take(MAX_NUMBER_OF_TOKENS_CHAT_GPT)
    return newString
}
