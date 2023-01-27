package com.devinjapan.shared.data.repository

import android.graphics.Bitmap
import com.devinjapan.shared.data.error.ImageDetectionException
import com.devinjapan.shared.data.source.local.ImageProcessorDataSource
import com.devinjapan.shared.domain.repository.ImageDetectorRepository
import com.devinjapan.shared.domain.util.Resource

class ImageDetectorRepositoryImpl(
    private val imageProcessor: ImageProcessorDataSource,
    private val analyticsTracker: com.devinjapan.shared.analytics.AnalyticsTracker
) : ImageDetectorRepository {

    override suspend fun getTagsFromImage(bitmap: Bitmap): Resource<List<String>> {
        return try {
            val resource = Resource.Success(
                data = imageProcessor.processBitmap(
                    bitmap
                )
            )
            analyticsTracker.logApiCall("getTagsFromImage", resource.data.toString())
            resource
        } catch (e: Exception) {
            e.printStackTrace()
            analyticsTracker.logApiCallError("getTagsFromImage", null)
            Resource.Error(message = e.message ?: "", ImageDetectionException())
        }
    }
}
