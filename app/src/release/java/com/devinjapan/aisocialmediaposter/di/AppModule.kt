package com.devinjapan.aisocialmediaposter.di

import android.content.Context
import com.devinjapan.aisocialmediaposter.BuildConfig
import com.devinjapan.aisocialmediaposter.data.interceptors.AuthorizationInterceptor
import com.devinjapan.aisocialmediaposter.data.interceptors.NetworkConnectivityInterceptor
import com.devinjapan.aisocialmediaposter.data.source.remote.OpenAIApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReleaseAppModule {
    @Singleton
    @Provides
    fun makeOkHttpClient(@ApplicationContext appContext: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor(BuildConfig.OpenApiSecret))
            .addInterceptor(NetworkConnectivityInterceptor(appContext))
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenAIApi(
        client: OkHttpClient
    ): OpenAIApi {
        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create()
    }
}
