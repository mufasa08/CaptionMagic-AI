package com.devinjapan.aisocialmediaposter.di

import android.content.Context
import com.devinjapan.aisocialmediaposter.data.repository.AuthRepositoryImpl
import com.devinjapan.aisocialmediaposter.data.repository.DataStoreRepositoryImpl
import com.devinjapan.aisocialmediaposter.domain.repository.AuthRepository
import com.devinjapan.aisocialmediaposter.domain.repository.DatastoreRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContext(
        @ApplicationContext context: Context
    ): Context {
        return context
    }

    @Provides
    @Singleton
    fun providesFirebaseAuthInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun providesDatastoreRepo(
        @ApplicationContext context: Context
    ): DatastoreRepository = DataStoreRepositoryImpl(context)

    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth
    ): AuthRepository = AuthRepositoryImpl(auth)
}
