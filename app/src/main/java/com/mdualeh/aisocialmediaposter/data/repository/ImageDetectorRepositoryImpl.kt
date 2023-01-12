package com.mdualeh.aisocialmediaposter.data.repository

import android.graphics.Bitmap
import com.mdualeh.aisocialmediaposter.data.source.local.ImageProcessorDataSource
import com.mdualeh.aisocialmediaposter.domain.repository.ImageDetectorRepository
import com.mdualeh.aisocialmediaposter.domain.util.Resource
import javax.inject.Inject

class ImageDetectorRepositoryImpl @Inject constructor(
    private val imageProcessor: ImageProcessorDataSource,
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
            Resource.Error(e.message ?: "An unknown error occurred.")
        } finally {
            imageProcessor.stop()
        }
    }
}
