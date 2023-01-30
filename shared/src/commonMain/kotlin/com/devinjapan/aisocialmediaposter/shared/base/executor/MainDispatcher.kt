package com.devinjapan.aisocialmediaposter.shared.base.executor

import kotlinx.coroutines.CoroutineDispatcher

expect class MainDispatcher() {
    val dispatcher: CoroutineDispatcher
}
