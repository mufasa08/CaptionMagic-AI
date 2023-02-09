package com.devinjapan.aisocialmediaposter.shared.di

import android.net.ConnectivityManager
import com.devinjapan.aisocialmediaposter.shared.plugin.networkConnectivityPlugin
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun httpClient(connectivityManager: ConnectivityManager) = HttpClient(OkHttp) {
    engine {
        config {
            retryOnConnectionFailure(true)
        }
    }

    install(networkConnectivityPlugin(connectivityManager))

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
