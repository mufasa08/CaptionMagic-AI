package com.devinjapan.aisocialmediaposter.di

import android.content.Context
import com.devinjapan.aisocialmediaposter.BuildConfig
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
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
            .addInterceptor(
                com.example.shared.data.interceptors.AuthorizationInterceptor(
                    BuildConfig.OpenApiSecret
                )
            )
            .addInterceptor(
                com.example.shared.data.interceptors.NetworkConnectivityInterceptor(
                    appContext
                )
            )
            .addInterceptor(
                FlipperOkhttpInterceptor(networkFlipperPlugin)
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenAIApi(
        client: OkHttpClient
    ): com.example.shared.data.source.remote.OpenAIApi {
        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create()
    }
}
