package com.mdualeh.aisocialmediaposter.domain.repository

import com.mdualeh.aisocialmediaposter.domain.weather.TextCompletion
import com.mdualeh.aisocialmediaposter.domain.util.Resource

interface TextCompletionRepository {
    suspend fun getReplyFromTextCompletionAPI(keywords: List<String>, maxWords: Int): Resource<TextCompletion>
}
