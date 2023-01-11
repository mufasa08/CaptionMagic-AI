package com.mdualeh.aisocialmediaposter.di

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.mdualeh.aisocialmediaposter.BuildConfig
import com.mdualeh.aisocialmediaposter.data.interceptors.AuthorizationInterceptor
import com.plcoding.weatherapp.data.remote.OpenAIApi
import com.squareup.moshi.Moshi
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
    fun provideNetworkFlipperPlugin(): NetworkFlipperPlugin = NetworkFlipperPlugin()

    @Singleton
    @Provides
    fun makeOkHttpClient(
        networkFlipperPlugin: NetworkFlipperPlugin,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor(BuildConfig.OpenApiSecret))
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
