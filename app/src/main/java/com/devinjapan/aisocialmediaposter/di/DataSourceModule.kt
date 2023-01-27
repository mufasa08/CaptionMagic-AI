package com.devinjapan.aisocialmediaposter.di

import android.content.Context
import com.example.shared.data.source.local.ImageProcessorDataSource
import com.example.shared.data.source.local.label.LabelDetectorProcessor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideLabelDetectorDataSource(@ApplicationContext appContext: Context): ImageProcessorDataSource {
        return LabelDetectorProcessor(appContext)
    }
}
