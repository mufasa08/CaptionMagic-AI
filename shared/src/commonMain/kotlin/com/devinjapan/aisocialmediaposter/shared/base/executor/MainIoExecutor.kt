package com.devinjapan.aisocialmediaposter.shared.base.executor

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

abstract class MainIoExecutor :
    com.devinjapan.aisocialmediaposter.shared.base.executor.IExecutorScope,
    CoroutineScope,
    KoinComponent {

    private val mainDispatcher: com.devinjapan.aisocialmediaposter.shared.base.executor.MainDispatcher by inject()
    private val ioDispatcher: CoroutineDispatcher by inject()

    private val job: CompletableJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + mainDispatcher.dispatcher

    override fun cancel() {
        job.cancel()
    }

    protected fun <T> collect(
        flow: Flow<T>,
        collect: (T) -> Unit
    ) {
        launch {
            flow
                .flowOn(ioDispatcher)
                .collect {
                    collect(it)
                }
        }
    }
}
