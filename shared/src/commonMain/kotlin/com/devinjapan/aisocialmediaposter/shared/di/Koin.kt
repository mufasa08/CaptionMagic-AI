package com.devinjapan.aisocialmediaposter.shared.di

import com.devinjapan.aisocialmediaposter.shared.data.repository.AuthRepositoryImpl
import com.devinjapan.aisocialmediaposter.shared.data.repository.DataStoreRepositoryImpl
import com.devinjapan.aisocialmediaposter.shared.data.repository.ImageDetectorRepositoryImpl
import com.devinjapan.aisocialmediaposter.shared.data.repository.TextCompletionRepositoryImpl
import com.devinjapan.aisocialmediaposter.shared.domain.repository.AuthRepository
import com.devinjapan.aisocialmediaposter.shared.domain.repository.DataStoreRepository
import com.devinjapan.aisocialmediaposter.shared.domain.repository.ImageDetectorRepository
import com.devinjapan.aisocialmediaposter.shared.domain.repository.TextCompletionRepository
import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            platformModule(),
            repositoryModule,
            dispatcherModule
        )
    }

// IOS
fun initKoin() = initKoin {}
val repositoryModule = module {

    single { Settings() }

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<DataStoreRepository> { DataStoreRepositoryImpl(get()) }
    single<TextCompletionRepository> { TextCompletionRepositoryImpl(get(), get(), get(), get()) }
    single<ImageDetectorRepository> { ImageDetectorRepositoryImpl(get(), get()) }
}

val dispatcherModule = module {
    factory { Dispatchers.Default }
}
