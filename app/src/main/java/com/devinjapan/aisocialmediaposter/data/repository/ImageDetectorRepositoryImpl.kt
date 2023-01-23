package com.devinjapan.aisocialmediaposter.data.repository

import android.graphics.Bitmap
import com.devinjapan.aisocialmediaposter.data.error.ImageDetectionException
import com.devinjapan.aisocialmediaposter.data.source.local.ImageProcessorDataSource
import com.devinjapan.aisocialmediaposter.domain.repository.ImageDetectorRepository
import com.devinjapan.aisocialmediaposter.domain.util.Resource
import javax.inject.Inject

class ImageDetectorRepositoryImpl @Inject constructor(
    private val imageProcessor: ImageProcessorDataSource
) : ImageDetectorRepository {

    override suspend fun getTagsFromImage(bitmap: Bitmap): Resource<List<String>> {
        return try {
            Resource.Success(
                data = imageProcessor.processBitmap(
                    bitmap
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = e.message ?: "", ImageDetectionException())
        }
    }
}
