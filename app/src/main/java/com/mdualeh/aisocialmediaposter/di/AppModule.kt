package com.mdualeh.aisocialmediaposter.di

import com.mdualeh.aisocialmediaposter.data.interceptors.AuthorizationInterceptor
import com.plcoding.weatherapp.data.remote.OpenAIApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton
import com.mdualeh.aisocialmediaposter.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private fun makeOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor(BuildConfig.OpenApiSecret)) //<----HERE
            .build()
    }
    @Provides
    @Singleton
    fun provideOpenAIApi(): OpenAIApi {
        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/completions")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(makeOkHttpClient())
            .build()
            .create()
    }
}