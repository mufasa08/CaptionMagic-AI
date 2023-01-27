package com.devinjapan.shared.di

import android.annotation.SuppressLint
import android.content.Context
import com.devinjapan.shared.analytics.AnalyticsTracker
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import daniel.avila.ricknmortykmm.shared.base.executor.MainDispatcher
import org.koin.core.module.Module
import org.koin.dsl.module

@SuppressLint("MissingPermission")
actual fun platformModule(): Module = module {
    val firebaseModule = module {
        single {
            val context = get<Context>()
            FirebaseAnalytics.getInstance(context)
        }

        single { AnalyticsTracker(get()) }

        single {
            FirebaseAuth.getInstance()
        }
    }
    single { MainDispatcher() }
}
