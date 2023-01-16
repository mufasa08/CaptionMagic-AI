package com.mdualeh.aisocialmediaposter.ui.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.exifinterface.media.ExifInterface

@RequiresApi(Build.VERSION_CODES.Q)
fun readExif(resolver: ContentResolver, id: Long): String? {
    val contentUri: Uri = ContentUris.withAppendedId(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        id,
    )

    var photoUri = MediaStore.setRequireOriginal(contentUri)
    var a: String? = ""
    var b: String? = ""
    var c: String? = ""
    resolver.openInputStream(photoUri)?.use { stream ->
        val exifInterface = ExifInterface(stream)
        a = exifInterface.getAttribute(ExifInterface.TAG_DATETIME)
        b = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
        c = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
    }

    return a
}