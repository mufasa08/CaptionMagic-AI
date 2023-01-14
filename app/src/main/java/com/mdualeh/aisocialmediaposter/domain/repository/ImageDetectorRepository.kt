package com.mdualeh.aisocialmediaposter.domain.repository

import android.graphics.Bitmap
import com.mdualeh.aisocialmediaposter.domain.util.Resource

interface ImageDetectorRepository {
    suspend fun getTagsFromImage(bitmap: Bitmap): Resource<List<String>>
}
