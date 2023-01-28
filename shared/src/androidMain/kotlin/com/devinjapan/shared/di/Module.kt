package com.devinjapan.shared.di

import android.annotation.SuppressLint
import android.content.Context
import com.devinjapan.shared.analytics.AnalyticsTracker
import com.devinjapan.shared.base.executor.MainDispatcher
import com.devinjapan.shared.data.source.local.ImageProcessorDataSource
import com.devinjapan.shared.data.source.local.label.LabelDetectorProcessor
import com.devinjapan.shared.data.source.remote.AuthDataSource
import com.devinjapan.shared.data.source.remote.FirebaseAuthDataSourceImpl
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import org.koin.core.module.Module
import org.koin.dsl.module

@SuppressLint("MissingPermission")
actual fun platformModule(): Module = module {
    single {
        val context = get<Context>()
        FirebaseAnalytics.getInstance(context)
    }

    single { AnalyticsTracker(get()) }

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
    single { MainDispatcher() }
}
