package com.mdualeh.aisocialmediaposter.di

import android.content.Context
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.mdualeh.aisocialmediaposter.data.source.local.ImageProcessorDataSource
import com.mdualeh.aisocialmediaposter.data.source.local.label.LabelDetectorProcessor
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
    fun provideLabelDetectorDataSource(@ApplicationContext appContext: Context): ImageProcessorDataSource =
        LabelDetectorProcessor(appContext, ImageLabelerOptions.DEFAULT_OPTIONS)
}
