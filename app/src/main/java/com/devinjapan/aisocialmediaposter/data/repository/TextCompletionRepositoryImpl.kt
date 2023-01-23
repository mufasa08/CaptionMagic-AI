package com.devinjapan.aisocialmediaposter.data.repository

import com.devinjapan.aisocialmediaposter.analytics.AnalyticsTracker
import com.devinjapan.aisocialmediaposter.data.error.toApiException
import com.devinjapan.aisocialmediaposter.data.mappers.toTextCompletion
import com.devinjapan.aisocialmediaposter.data.request.TextCompletionRequestBody
import com.devinjapan.aisocialmediaposter.data.utils.MAX_NUMBER_OF_TOKENS_CHAT_GPT
import com.devinjapan.aisocialmediaposter.data.utils.toChatGPTUnderstandableString
import com.devinjapan.aisocialmediaposter.domain.model.SocialMedia
import com.devinjapan.aisocialmediaposter.domain.model.TextCompletion
import com.devinjapan.aisocialmediaposter.domain.repository.DatastoreRepository
import com.devinjapan.aisocialmediaposter.domain.repository.TextCompletionRepository
import com.devinjapan.aisocialmediaposter.domain.util.Resource
import com.devinjapan.aisocialmediaposter.ui.utils.HIDE_PROMO_HASHTAGS
import com.devinjapan.aisocialmediaposter.ui.utils.SELECTED_TONE
import com.plcoding.weatherapp.data.remote.OpenAIApi
import retrofit2.HttpException
import javax.inject.Inject

class TextCompletionRepositoryImpl @Inject constructor(
    private val authRepositoryImpl: AuthRepositoryImpl,
    private val dataStoreRepository: DatastoreRepository,
    private val api: OpenAIApi,
    private val analyticsTracker: AnalyticsTracker
) : TextCompletionRepository {

    override suspend fun getReplyFromTextCompletionAPI(
        keywords: List<String>,
        maxCharacters: Int,
        type: SocialMedia
    ): Resource<TextCompletion> {
        return try {
            val selectedTone = dataStoreRepository.getString(SELECTED_TONE)
            val user = authRepositoryImpl.getSignedInUserIfExists()
            val hideHashTags = dataStoreRepository.getBoolean(HIDE_PROMO_HASHTAGS) ?: false
            val resource = Resource.Success(
                data = api.postTextCompletionReply(
                    textCompletionRequestBody = TextCompletionRequestBody(
                        // fix magic number
                        maxTokens = minOf(maxCharacters, MAX_NUMBER_OF_TOKENS_CHAT_GPT),
                        prompt = type.toChatGPTUnderstandableString(selectedTone, keywords),
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
                (e as HttpException).code()
            )
            Resource.Error(message = e.message ?: "Something went wrong.", exception = apiException)
        }
    }
}
