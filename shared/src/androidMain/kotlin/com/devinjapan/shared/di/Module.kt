package com.devinjapan.shared.di

import daniel.avila.ricknmortykmm.shared.base.executor.MainDispatcher
import daniel.avila.ricknmortykmm.shared.data_cache.sqldelight.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { DatabaseDriverFactory(get()) }
    single { MainDispatcher() }
}
