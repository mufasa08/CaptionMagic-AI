package com.devinjapan.aisocialmediaposter

import android.app.Application
import com.devinjapan.aisocialmediaposter.di.viewModelModule
import com.devinjapan.shared.di.initKoin
import com.devinjapan.shared.domain.repository.AuthRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext

open class App : Application() {

    private val authRepository: AuthRepository by inject()

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        GlobalScope.launch {
            authRepository.signInAnonymously()
        }

        initKoin {
            // androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(
                viewModelModule
            )
        }
    }
}
