package com.devinjapan.shared.data.repository

import com.devinjapan.shared.analytics.AnalyticsTracker
import com.devinjapan.shared.data.error.toApiException
import com.devinjapan.shared.data.mappers.toTextCompletion
import com.devinjapan.shared.data.request.TextCompletionRequestBody
import com.devinjapan.shared.data.source.remote.OpenAIApi
import com.devinjapan.shared.data.utils.MAX_NUMBER_OF_TOKENS_CHAT_GPT
import com.devinjapan.shared.data.utils.toChatGPTUnderstandableString
import com.devinjapan.shared.domain.model.SocialMedia
import com.devinjapan.shared.domain.model.TextCompletion
import com.devinjapan.shared.domain.repository.DataStoreRepository
import com.devinjapan.shared.domain.repository.TextCompletionRepository
import com.devinjapan.shared.domain.util.Resource

class TextCompletionRepositoryImpl(
    private val authRepositoryImpl: AuthRepositoryImpl,
    private val dataStoreRepository: DataStoreRepository,
    private val api: OpenAIApi,
    private val analyticsTracker: AnalyticsTracker
) : TextCompletionRepository {

    override suspend fun getReplyFromTextCompletionAPI(
        keywords: List<String>,
        maxWords: Int,
        type: SocialMedia
    ): Resource<TextCompletion> {
        return try {
            val selectedTone = dataStoreRepository.getString(SELECTED_TONE)
            val user = authRepositoryImpl.getSignedInUserIfExists()
            val hideHashTags = dataStoreRepository.getBoolean(HIDE_PROMO_HASHTAGS) ?: false
            val prompt =
                type.toChatGPTUnderstandableString(selectedTone, keywords).cleanPromptString()
            val resource = Resource.Success(
                data = api.postTextCompletionReply(
                    textCompletionRequestBody = TextCompletionRequestBody(
                        // fix magic number
                        maxTokens = minOf(maxWords, MAX_NUMBER_OF_TOKENS_CHAT_GPT),
                        prompt = prompt,
                        user = user?.userId ?: "not-signed-in"
                    )
                ).toTextCompletion(hideHashTags)
            )
            analyticsTracker.logApiCall("getReplyFromTextCompletionAPI", type.name)
            resource
        } catch (e: Exception) {
            e.printStackTrace()
            val apiException = e.toApiException()

            analyticsTracker.logApiCallError(
                "getReplyFromTextCompletionAPI",
                type.name,
                apiException.type.name
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
