package com.devinjapan.shared.data.util

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

val defaultExceptionHandler = CoroutineExceptionHandler { _, e ->
    Log.d(e.message, "uncaught coroutine exception")
    FirebaseCrashlytics.getInstance().recordException(e)
}

fun SafeMainScope() = CoroutineScope(SupervisorJob() + Dispatchers.Main + defaultExceptionHandler)
