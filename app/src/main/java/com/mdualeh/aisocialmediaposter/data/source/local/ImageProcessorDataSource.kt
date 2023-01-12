package com.mdualeh.aisocialmediaposter.data.source.local

import android.graphics.Bitmap

/** An interface to process the images with different vision detectors and custom image models.  */
interface ImageProcessorDataSource {
    /** Processes a bitmap image.  */
    suspend fun processBitmap(bitmap: Bitmap?): List<String>

    /** Stops the underlying machine learning model and release resources.  */
    // TODO use this
    fun stop()
}
