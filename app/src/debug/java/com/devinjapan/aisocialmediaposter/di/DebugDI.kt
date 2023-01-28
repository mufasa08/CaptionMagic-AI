package com.devinjapan.aisocialmediaposter.di

import android.content.Context
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DebugDI {
    @Singleton
    @Provides
    fun provideNetworkFlipperPlugin(): NetworkFlipperPlugin = NetworkFlipperPlugin()

    @Singleton
    @Provides
    fun makeKtorHttpClient(
        @ApplicationContext appContext: Context
    ): HttpClient {
        return HttpClient() {
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
            /*return OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor(BuildConfig.OpenApiSecret))
            .addInterceptor(NetworkConnectivityInterceptor(appContext))
            .addInterceptor(
                FlipperOkhttpInterceptor(networkFlipperPlugin)
            )
            .build()*/
        }
    }
}
