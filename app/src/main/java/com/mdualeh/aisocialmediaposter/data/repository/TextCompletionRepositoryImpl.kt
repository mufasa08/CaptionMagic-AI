package com.plcoding.weatherapp.data.repository

import com.mdualeh.aisocialmediaposter.data.mappers.toTextCompletion
import com.mdualeh.aisocialmediaposter.domain.repository.TextCompletionRepository
import com.mdualeh.aisocialmediaposter.domain.util.Resource
import com.mdualeh.aisocialmediaposter.domain.weather.TextCompletion
import com.plcoding.weatherapp.data.remote.OpenAIApi
import javax.inject.Inject

class TextCompletionRepositoryImpl @Inject constructor(
    private val api: OpenAIApi
) : TextCompletionRepository {

    override suspend fun getReplyFromTextCompletionAPI(keywords: List<String>, maxWords: Int): Resource<TextCompletion> {
        return try {
            Resource.Success(
                data = api.getTextCompletionReply(
                    keywords = keywords.joinToString(separator = " "),
                    maxWords = maxWords,
                ).toTextCompletion()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}
