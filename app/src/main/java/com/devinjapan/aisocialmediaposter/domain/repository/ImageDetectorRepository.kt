package com.devinjapan.aisocialmediaposter.domain.repository

import android.graphics.Bitmap
import com.devinjapan.aisocialmediaposter.domain.util.Resource

interface ImageDetectorRepository {
    suspend fun getTagsFromImage(bitmap: Bitmap): Resource<List<String>>
}
