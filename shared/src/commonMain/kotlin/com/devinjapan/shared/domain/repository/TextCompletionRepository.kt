package com.devinjapan.shared.domain.repository

import com.example.shared.domain.model.SocialMedia
import com.example.shared.domain.model.TextCompletion
import com.example.shared.domain.util.Resource

interface TextCompletionRepository {
    suspend fun getReplyFromTextCompletionAPI(
        keywords: List<String>,
        maxWords: Int,
        type: SocialMedia
    ): Resource<TextCompletion>
}
