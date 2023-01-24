package com.devinjapan.aisocialmediaposter.data.repository

import android.graphics.Bitmap
import com.devinjapan.aisocialmediaposter.analytics.AnalyticsTracker
import com.devinjapan.aisocialmediaposter.data.error.ImageDetectionException
import com.devinjapan.aisocialmediaposter.data.source.local.ImageProcessorDataSource
import com.devinjapan.aisocialmediaposter.domain.repository.ImageDetectorRepository
import com.devinjapan.aisocialmediaposter.domain.util.Resource
import javax.inject.Inject

class ImageDetectorRepositoryImpl @Inject constructor(
    private val imageProcessor: ImageProcessorDataSource,
    private val analyticsTracker: AnalyticsTracker
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
