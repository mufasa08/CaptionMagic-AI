package com.mdualeh.aisocialmediaposter.domain.repository

import com.mdualeh.aisocialmediaposter.domain.weather.TextCompletion
import com.mdualeh.aisocialmediaposter.domain.util.Resource
import com.mdualeh.aisocialmediaposter.domain.weather.SocialMedia

interface TextCompletionRepository {
    suspend fun getReplyFromTextCompletionAPI(
        keywords: List<String>,
        maxWords: Int,
        type: SocialMedia
    ): Resource<TextCompletion>
}
