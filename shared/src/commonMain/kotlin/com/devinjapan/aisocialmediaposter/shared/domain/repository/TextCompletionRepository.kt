package com.devinjapan.aisocialmediaposter.shared.domain.repository

import com.devinjapan.aisocialmediaposter.shared.domain.model.SocialMedia
import com.devinjapan.aisocialmediaposter.shared.domain.model.TextCompletion
import com.devinjapan.aisocialmediaposter.shared.domain.util.Resource

interface TextCompletionRepository {
    suspend fun getReplyFromTextCompletionAPI(
        keywords: List<String>,
        maxWords: Int,
        type: SocialMedia
    ): Resource<TextCompletion>
}
