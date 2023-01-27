package com.devinjapan.shared.data.interceptors

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.getSystemService
import com.example.shared.data.error.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectivityInterceptor(
    context: Context
) : Interceptor {
    private val manager = context.getSystemService<ConnectivityManager>()

    @Suppress("DEPRECATION")
    private val connected
        get() = manager?.activeNetworkInfo?.isConnectedOrConnecting ?: false

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!connected) {
            throw NoInternetException()
        }
        return chain.proceed(chain.request())
    }
}
