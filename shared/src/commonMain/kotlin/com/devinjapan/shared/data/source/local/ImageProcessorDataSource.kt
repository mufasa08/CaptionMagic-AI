package com.devinjapan.shared.data.source.local

/** An interface to process the images with different vision detectors and custom image models.  */
interface ImageProcessorDataSource {
    /** Processes a bitmap image.  */
    suspend fun processImage(imageUri: String?): List<String>

    /** Stops the underlying machine learning model and release resources.  */
    // TODO use this
    fun stop()
}
