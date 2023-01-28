package com.devinjapan.shared.data.source.local.label

import com.devinjapan.shared.data.source.local.ImageProcessorDataSource

/** Custom InputImage Classifier Demo.  */
actual class LabelDetectorProcessor : ImageProcessorDataSource {
    override suspend fun processImage(imageUri: String?): List<String> {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }
}
