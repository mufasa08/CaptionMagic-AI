package com.mdualeh.aisocialmediaposter.data.repository

import com.mdualeh.aisocialmediaposter.data.mappers.toTextCompletion
import com.mdualeh.aisocialmediaposter.data.request.TextCompletionRequestBody
import com.mdualeh.aisocialmediaposter.data.utils.MAX_NUMBER_OF_TOKENS_CHAT_GPT
import com.mdualeh.aisocialmediaposter.data.utils.toChatGPTUnderstandableString
import com.mdualeh.aisocialmediaposter.domain.repository.TextCompletionRepository
import com.mdualeh.aisocialmediaposter.domain.util.Resource
import com.mdualeh.aisocialmediaposter.domain.weather.SocialMedia
import com.mdualeh.aisocialmediaposter.domain.weather.TextCompletion
import com.plcoding.weatherapp.data.remote.OpenAIApi
import javax.inject.Inject

class TextCompletionRepositoryImpl @Inject constructor(
    private val api: OpenAIApi
) : TextCompletionRepository {

    override suspend fun getReplyFromTextCompletionAPI(
        keywords: List<String>,
        maxCharacters: Int,
        type: SocialMedia
    ): Resource<TextCompletion> {
        return try {
            Resource.Success(
                data = api.postTextCompletionReply(
                    textCompletionRequestBody = TextCompletionRequestBody(
                        // fix magic number
                        maxTokens = minOf(maxCharacters, MAX_NUMBER_OF_TOKENS_CHAT_GPT),
                        prompt = type.toChatGPTUnderstandableString(keywords)
                    )
                ).toTextCompletion()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}
