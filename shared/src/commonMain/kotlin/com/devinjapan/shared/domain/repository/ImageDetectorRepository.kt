package com.devinjapan.shared.domain.repository

import android.graphics.Bitmap
import com.example.shared.domain.util.Resource

interface ImageDetectorRepository {
    suspend fun getTagsFromImage(bitmap: Bitmap): Resource<List<String>>
}
