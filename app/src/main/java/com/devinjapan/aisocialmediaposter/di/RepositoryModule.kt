package com.devinjapan.aisocialmediaposter.di

import com.example.shared.data.repository.ImageDetectorRepositoryImpl
import com.example.shared.data.repository.TextCompletionRepositoryImpl
import com.example.shared.domain.repository.ImageDetectorRepository
import com.example.shared.domain.repository.TextCompletionRepository
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
