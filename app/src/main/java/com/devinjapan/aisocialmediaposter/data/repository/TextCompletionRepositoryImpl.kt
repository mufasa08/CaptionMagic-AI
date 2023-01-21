package com.devinjapan.aisocialmediaposter.data.repository

import com.devinjapan.aisocialmediaposter.data.mappers.toTextCompletion
import com.devinjapan.aisocialmediaposter.data.request.TextCompletionRequestBody
import com.devinjapan.aisocialmediaposter.data.utils.MAX_NUMBER_OF_TOKENS_CHAT_GPT
import com.devinjapan.aisocialmediaposter.data.utils.toChatGPTUnderstandableString
import com.devinjapan.aisocialmediaposter.domain.model.SocialMedia
import com.devinjapan.aisocialmediaposter.domain.model.TextCompletion
import com.devinjapan.aisocialmediaposter.domain.repository.DatastoreRepository
import com.devinjapan.aisocialmediaposter.domain.repository.TextCompletionRepository
import com.devinjapan.aisocialmediaposter.domain.util.Resource
import com.devinjapan.aisocialmediaposter.ui.utils.SELECTED_TONE
import com.plcoding.weatherapp.data.remote.OpenAIApi
import javax.inject.Inject

class TextCompletionRepositoryImpl @Inject constructor(
    private val authRepositoryImpl: AuthRepositoryImpl,
    private val dataStoreRepository: DatastoreRepository,
    private val api: OpenAIApi
) : TextCompletionRepository {

    override suspend fun getReplyFromTextCompletionAPI(
        keywords: List<String>,
        maxCharacters: Int,
        type: SocialMedia
    ): Resource<TextCompletion> {
        return try {
            val selectedTone = dataStoreRepository.getString(SELECTED_TONE)
            val user = authRepositoryImpl.getSignedInUserIfExists()
            Resource.Success(
                data = api.postTextCompletionReply(
                    textCompletionRequestBody = TextCompletionRequestBody(
                        // fix magic number
                        maxTokens = minOf(maxCharacters, MAX_NUMBER_OF_TOKENS_CHAT_GPT),
                        prompt = type.toChatGPTUnderstandableString(selectedTone, keywords),
                        user = user?.userId ?: "not-signed-in"
                    )
                ).toTextCompletion()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}
