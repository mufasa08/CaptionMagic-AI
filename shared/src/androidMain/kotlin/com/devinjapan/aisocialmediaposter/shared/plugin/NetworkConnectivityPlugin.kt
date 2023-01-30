package com.devinjapan.aisocialmediaposter.shared.plugin

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import com.devinjapan.aisocialmediaposter.shared.data.error.NoInternetException
import io.ktor.client.engine.*
import io.ktor.client.plugins.api.*

@SuppressLint("MissingPermission")
fun networkConnectivityPlugin(manager: ConnectivityManager) =
    createClientPlugin("network_connectivity") {
        onRequest { request, content ->
            val connected = manager.activeNetworkInfo?.isConnectedOrConnecting ?: false
            if (!connected) {
                throw NoInternetException()
            }
        }
    }
