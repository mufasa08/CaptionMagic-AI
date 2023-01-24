package com.devinjapan.aisocialmediaposter.ui.utils

import android.graphics.Bitmap

fun Bitmap.isLandscape(): Boolean = this.width > this.height
