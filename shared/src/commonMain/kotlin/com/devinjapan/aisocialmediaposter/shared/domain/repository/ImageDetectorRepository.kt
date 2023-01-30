package com.devinjapan.aisocialmediaposter.shared.domain.repository

import com.devinjapan.aisocialmediaposter.shared.domain.util.Resource

interface ImageDetectorRepository {
    suspend fun getTagsFromImage(imageUri: String): Resource<List<String>>
}
