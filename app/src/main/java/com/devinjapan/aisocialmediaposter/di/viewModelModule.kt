package com.devinjapan.aisocialmediaposter.di

import com.devinjapan.aisocialmediaposter.ui.viewmodels.CaptionGeneratorViewModel
import com.devinjapan.aisocialmediaposter.ui.viewmodels.SettingsViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { CaptionGeneratorViewModel() }
    single { SettingsViewModel() }
}
