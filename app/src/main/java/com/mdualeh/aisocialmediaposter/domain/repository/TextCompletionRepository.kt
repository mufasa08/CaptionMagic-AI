package com.mdualeh.aisocialmediaposter.domain.repository

import com.mdualeh.aisocialmediaposter.domain.model.SocialMedia
import com.mdualeh.aisocialmediaposter.domain.model.TextCompletion
import com.mdualeh.aisocialmediaposter.domain.util.Resource

interface TextCompletionRepository {
    suspend fun getReplyFromTextCompletionAPI(
        keywords: List<String>,
        maxWords: Int,
        type: SocialMedia
    ): Resource<TextCompletion>
}
