package com.devinjapan.aisocialmediaposter.di

import android.content.Context
import com.example.shared.analytics.AnalyticsTracker
import com.example.shared.data.repository.AuthRepositoryImpl
import com.example.shared.data.repository.DataStoreRepositoryImpl
import com.example.shared.domain.repository.AuthRepository
import com.example.shared.domain.repository.DatastoreRepository
import com.google.firebase.analytics.FirebaseAnalytics
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

    @Singleton
    @Provides
    fun provideFirebaseAnalytics(
        @ApplicationContext context: Context
    ): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    @Singleton
    @Provides
    fun provideAnalyticsTracker(firebaseAnalytics: FirebaseAnalytics): AnalyticsTracker =
        AnalyticsTracker(firebaseAnalytics)

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
        auth: FirebaseAuth,
        analyticsTracker: AnalyticsTracker
    ): AuthRepository = AuthRepositoryImpl(auth, analyticsTracker)
}
