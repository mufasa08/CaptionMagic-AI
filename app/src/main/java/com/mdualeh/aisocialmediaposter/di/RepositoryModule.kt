package com.mdualeh.aisocialmediaposter.di

import com.mdualeh.aisocialmediaposter.domain.repository.TextCompletionRepository
import com.plcoding.weatherapp.data.repository.TextCompletionRepositoryImpl
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
    abstract fun bindTextCompletionRepository(
        textCompletionRepository: TextCompletionRepositoryImpl
    ): TextCompletionRepository
}
