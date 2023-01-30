package com.devinjapan.aisocialmediaposter.shared.data.repository

import com.devinjapan.aisocialmediaposter.shared.data.error.ImageDetectionException
import com.devinjapan.aisocialmediaposter.shared.data.source.local.ImageProcessorDataSource
import com.devinjapan.aisocialmediaposter.shared.domain.repository.ImageDetectorRepository
import com.devinjapan.aisocialmediaposter.shared.domain.util.Resource

class ImageDetectorRepositoryImpl(
    private val imageProcessor: ImageProcessorDataSource,
    private val analyticsTracker: com.devinjapan.aisocialmediaposter.shared.analytics.AnalyticsTracker
) : ImageDetectorRepository {

    override suspend fun getTagsFromImage(imageUri: String): Resource<List<String>> {
        return try {
            val resource = Resource.Success(
                data = imageProcessor.processImage(
                    imageUri
                )
            )
            analyticsTracker.logApiCall("getTagsFromImage", resource.data.toString())
            resource
        } catch (e: Exception) {
            e.printStackTrace()
            analyticsTracker.logApiCallError("getTagsFromImage", null, null, null)
            Resource.Error(message = e.message ?: "", ImageDetectionException())
        }
    }
}
