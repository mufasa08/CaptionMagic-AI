package com.devinjapan.aisocialmediaposter.di

import com.devinjapan.aisocialmediaposter.BuildConfig
import com.devinjapan.aisocialmediaposter.data.interceptors.AuthorizationInterceptor
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

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
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
