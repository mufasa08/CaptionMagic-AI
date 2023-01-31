package com.devinjapan.aisocialmediaposter.ui.utils

import android.content.ContentResolver
import android.os.Build
import android.os.ext.SdkExtensions.getExtensionVersion

private fun isPhotoPickerAvailable(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        true
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        getExtensionVersion(Build.VERSION_CODES.R) >= 2
    } else {
        false
    }
}

fun handlePhotoPickerLaunch(contentResolver: ContentResolver) {
    if (isPhotoPickerAvailable()) {
        // To launch the system photo picker, invoke an intent that includes the
        // ACTION_PICK_IMAGES action. Consider adding support for the
        // EXTRA_PICK_IMAGES_MAX intent extra.
    } else {
        // Consider implementing fallback functionality so that users can still
        // select images and videos.
    }
}
