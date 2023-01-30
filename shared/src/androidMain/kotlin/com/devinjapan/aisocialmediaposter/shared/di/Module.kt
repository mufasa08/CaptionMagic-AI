package com.devinjapan.aisocialmediaposter.shared.di

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import com.devinjapan.aisocialmediaposter.shared.data.source.local.ImageProcessorDataSource
import com.devinjapan.aisocialmediaposter.shared.data.source.local.label.LabelDetectorProcessor
import com.devinjapan.aisocialmediaposter.shared.data.source.remote.AuthDataSource
import com.devinjapan.aisocialmediaposter.shared.data.source.remote.FirebaseAuthDataSourceImpl
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.*
import org.koin.core.module.Module
import org.koin.dsl.module

@SuppressLint("MissingPermission")
actual fun platformModule(): Module = module {
    single {
        val context = get<Context>()
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        httpClient(connectivityManager)
    }

    single {
        val context = get<Context>()
        FirebaseAnalytics.getInstance(context)
    }

    single { com.devinjapan.aisocialmediaposter.shared.analytics.AnalyticsTracker(get()) }

    single {
        FirebaseAuth.getInstance()
    }

    single<AuthDataSource> {
        FirebaseAuthDataSourceImpl(get())
    }

    single<ImageProcessorDataSource> {
        val context = get<Context>()
        LabelDetectorProcessor(context)
    }
    single { com.devinjapan.aisocialmediaposter.shared.base.executor.MainDispatcher() }
}
