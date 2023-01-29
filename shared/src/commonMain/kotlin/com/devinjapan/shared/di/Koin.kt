package com.devinjapan.shared.di

import com.devinjapan.shared.data.repository.AuthRepositoryImpl
import com.devinjapan.shared.data.repository.DataStoreRepositoryImpl
import com.devinjapan.shared.data.repository.ImageDetectorRepositoryImpl
import com.devinjapan.shared.data.repository.TextCompletionRepositoryImpl
import com.devinjapan.shared.domain.repository.AuthRepository
import com.devinjapan.shared.domain.repository.DataStoreRepository
import com.devinjapan.shared.domain.repository.ImageDetectorRepository
import com.devinjapan.shared.domain.repository.TextCompletionRepository
import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            repositoryModule,
            dispatcherModule,
            platformModule()
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

    single {
        HttpClient() {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        encodeDefaults = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
                filter { request ->
                    request.url.host.contains("ktor.io")
                }
            }
        }
    }
}

val dispatcherModule = module {
    factory { Dispatchers.Default }
}