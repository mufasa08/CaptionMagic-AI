package com.devinjapan.shared.domain.repository

import com.devinjapan.shared.domain.util.Resource

interface ImageDetectorRepository {
    suspend fun getTagsFromImage(imageUri: String): Resource<List<String>>
}
