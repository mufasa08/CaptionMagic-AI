package com.devinjapan.aisocialmediaposter.domain.util

import com.devinjapan.aisocialmediaposter.data.error.ApiException

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val exception: ApiException? = null
) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String, exception: ApiException? = null, data: T? = null) :
        Resource<T>(data, message, exception)
}
