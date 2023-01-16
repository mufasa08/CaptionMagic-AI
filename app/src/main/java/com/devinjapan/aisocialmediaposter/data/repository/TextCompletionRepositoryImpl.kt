package com.devinjapan.aisocialmediaposter.data.repository

import com.devinjapan.aisocialmediaposter.data.mappers.toTextCompletion
import com.devinjapan.aisocialmediaposter.data.request.TextCompletionRequestBody
import com.devinjapan.aisocialmediaposter.data.utils.MAX_NUMBER_OF_TOKENS_CHAT_GPT
import com.devinjapan.aisocialmediaposter.data.utils.toChatGPTUnderstandableString
import com.devinjapan.aisocialmediaposter.domain.model.SocialMedia
import com.devinjapan.aisocialmediaposter.domain.model.TextCompletion
import com.devinjapan.aisocialmediaposter.domain.repository.TextCompletionRepository
import com.devinjapan.aisocialmediaposter.domain.util.Resource
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
