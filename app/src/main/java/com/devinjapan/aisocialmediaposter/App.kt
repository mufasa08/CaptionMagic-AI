package com.devinjapan.aisocialmediaposter

import android.app.Application
import com.devinjapan.aisocialmediaposter.domain.repository.AuthRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

open class App : Application() {

    @Inject
    lateinit var authRepository: AuthRepository

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        GlobalScope.launch {
            authRepository.signInAnonymously()
        }
    }
}
