package com.devinjapan.aisocialmediaposter.shared.base.executor

import kotlinx.coroutines.CoroutineDispatcher

actual class MainDispatcher actual constructor() {
    actual val dispatcher: CoroutineDispatcher
        get() = TODO("Not yet implemented")
}
