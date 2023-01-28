package com.devinjapan.shared.data.repository

import com.devinjapan.shared.analytics.AnalyticsTracker
import com.devinjapan.shared.data.error.ImageDetectionException
import com.devinjapan.shared.data.source.local.ImageProcessorDataSource
import com.devinjapan.shared.domain.repository.ImageDetectorRepository
import com.devinjapan.shared.domain.util.Resource

class ImageDetectorRepositoryImpl(
    private val imageProcessor: ImageProcessorDataSource,
    private val analyticsTracker: AnalyticsTracker
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
            analyticsTracker.logApiCallError("getTagsFromImage", null)
            Resource.Error(message = e.message ?: "", ImageDetectionException())
        }
    }
}
