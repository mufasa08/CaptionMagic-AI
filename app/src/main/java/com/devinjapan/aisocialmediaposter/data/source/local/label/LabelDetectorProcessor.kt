/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.devinjapan.aisocialmediaposter.data.source.local.label

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.devinjapan.aisocialmediaposter.data.source.local.ImageProcessorDataSource
import com.devinjapan.aisocialmediaposter.data.utils.DetectorType
import com.google.android.gms.tasks.Task
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.tasks.await
import java.io.IOException

/** Custom InputImage Classifier Demo.  */
class LabelDetectorProcessor(context: Context) :
    VisionProcessorBase<List<ImageLabel>>(context), ImageProcessorDataSource {

    private val localModel = LocalModel.Builder()
        .setAssetFilePath("food_v1.tflite")
        // or .setAbsoluteFilePath(absolute file path to model file)
        // or .setUri(URI to model file)
        .build()

    private val foodImageLabelerOptions = CustomImageLabelerOptions.Builder(localModel)
        .setConfidenceThreshold(0.5f)
        .setMaxResultCount(5)
        .build()

    private val foodImageLabeler: ImageLabeler = ImageLabeling.getClient(foodImageLabelerOptions)
    private val imageLabeler: ImageLabeler =
        ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

    override fun stop() {
        stopProcess()
        try {
            foodImageLabeler.close()
            imageLabeler.close()
        } catch (e: IOException) {
            Log.e(
                TAG,
                "Exception thrown while trying to close ImageLabelerClient: $e"
            )
        }
    }

    override fun detectInImage(
        image: InputImage,
        detectorType: DetectorType
    ): Task<List<ImageLabel>> {
        return when (detectorType) {
            DetectorType.FOOD -> foodImageLabeler.process(image)
            else -> imageLabeler.process(image)
        }
    }

    companion object {
        private const val TAG = "LabelDetectorProcessor"
        private const val KEYWORD_FOOD = "Food"

        private fun logExtrasForTesting(labels: List<ImageLabel>?) {
            if (labels == null) {
                Log.v(MANUAL_TESTING_LOG, "No labels detected")
            } else {
                for (label in labels) {
                    Log.v(
                        MANUAL_TESTING_LOG,
                        String.format("Label %s, confidence %f", label.text, label.confidence)
                    )
                }
            }
        }
    }

    override suspend fun processBitmap(bitmap: Bitmap?): List<String> {
        val baseList = detectInImage(
            InputImage.fromBitmap(bitmap!!, 0), DetectorType.BASIC
        ).await().map { it.text }

        if (baseList.contains(KEYWORD_FOOD)) {
            val foodList = detectInImage(
                InputImage.fromBitmap(bitmap, 0), DetectorType.FOOD
            ).await().map { it.text }
            return foodList + baseList
        }
        return baseList
    }
}
