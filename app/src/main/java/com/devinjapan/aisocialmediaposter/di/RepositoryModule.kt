package com.devinjapan.aisocialmediaposter.di

import com.devinjapan.aisocialmediaposter.data.repository.ImageDetectorRepositoryImpl
import com.devinjapan.aisocialmediaposter.data.repository.TextCompletionRepositoryImpl
import com.devinjapan.aisocialmediaposter.domain.repository.ImageDetectorRepository
import com.devinjapan.aisocialmediaposter.domain.repository.TextCompletionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindImageDetectorRepository(
        imageDetectorRepository: ImageDetectorRepositoryImpl
    ): ImageDetectorRepository

    @Binds
    @Singleton
    abstract fun bindTextCompletionRepository(
        textCompletionRepository: TextCompletionRepositoryImpl
    ): TextCompletionRepository
}
