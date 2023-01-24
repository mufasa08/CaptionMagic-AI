package com.devinjapan.aisocialmediaposter.di

import android.content.Context
import com.devinjapan.aisocialmediaposter.BuildConfig
import com.devinjapan.aisocialmediaposter.data.interceptors.AuthorizationInterceptor
import com.devinjapan.aisocialmediaposter.data.interceptors.NetworkConnectivityInterceptor
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.plcoding.weatherapp.data.remote.OpenAIApi
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
object DebugDI {
    @Singleton
    @Provides
    fun provideNetworkFlipperPlugin(): NetworkFlipperPlugin = NetworkFlipperPlugin()

    @Singleton
    @Provides
    fun makeOkHttpClient(
        networkFlipperPlugin: NetworkFlipperPlugin,
        @ApplicationContext appContext: Context
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor(BuildConfig.OpenApiSecret))
            .addInterceptor(NetworkConnectivityInterceptor(appContext))
            .addInterceptor(
                FlipperOkhttpInterceptor(networkFlipperPlugin)
            )
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
