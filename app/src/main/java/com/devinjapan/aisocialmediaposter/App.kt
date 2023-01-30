package com.devinjapan.aisocialmediaposter

import android.app.Application
import com.devinjapan.aisocialmediaposter.di.viewModelModule
import com.devinjapan.aisocialmediaposter.shared.di.dispatcherModule
import com.devinjapan.aisocialmediaposter.shared.di.platformModule
import com.devinjapan.aisocialmediaposter.shared.di.repositoryModule
import com.devinjapan.aisocialmediaposter.shared.domain.repository.AuthRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

open class App : Application() {

    private val authRepository: AuthRepository by inject()

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(
                platformModule(),
                repositoryModule,
                dispatcherModule,
                viewModelModule
            )
        }

        GlobalScope.launch {
            authRepository.signInAnonymously()
        }
    }
}
