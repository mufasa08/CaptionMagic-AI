package com.devinjapan.shared.domain.repository

import com.devinjapan.shared.domain.model.SocialMedia
import com.devinjapan.shared.domain.model.TextCompletion
import com.devinjapan.shared.domain.util.Resource

interface TextCompletionRepository {
    suspend fun getReplyFromTextCompletionAPI(
        keywords: List<String>,
        maxWords: Int,
        type: SocialMedia
    ): Resource<TextCompletion>
}
