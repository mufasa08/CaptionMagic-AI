package com.mdualeh.aisocialmediaposter.data.interceptors

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthorizationInterceptor(var apiKey: String) : Interceptor {

     override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $apiKey").build()
       return chain.proceed(request)
     }
}