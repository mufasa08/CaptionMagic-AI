package com.devinjapan.aisocialmediaposter.data.error

import com.squareup.moshi.JsonDataException
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ApiException(
    val type: Type,
    message: String? = type.name,
    cause: Throwable? = null
) : IOException(message, cause) {

    enum class Type(
        private val httpStatusRange: IntRange? = null,
        private vararg val detailCode: String
    ) {

        // HTTP errors
        BAD_REQUEST(400),
        UNAUTHORIZED(401),
        FORBIDDEN(403),
        CONFLICT(409),

        // Response body is empty or has invalid format
        NO_INTERNET_ERROR,

        // Response body is empty or has invalid format
        INVALID_RESPONSE,

        // Failed to connect to web service. There is internet connection, though
        CONNECTION_ERROR,

        // Other errors
        GENERAL_IO_ERROR,
        UNKNOWN_ERROR,
        ;

        constructor(httpStatus: Int, vararg detailCode: String) :
            this(httpStatus..httpStatus, *detailCode)

        private fun match(httpStatus: Int, code: String?): Boolean {
            return (httpStatusRange != null) &&
                httpStatusRange.contains(httpStatus) &&
                (detailCode.isEmpty() || detailCode.contains(code))
        }

        companion object {
            fun from(httpStatus: Int?, code: String? = null): Type {
                return httpStatus?.let { status ->
                    values().firstOrNull { it.match(status, code) }
                } ?: UNKNOWN_ERROR
            }

            fun from(e: Throwable): Type {
                return when (e) {
                    is JsonDataException -> INVALID_RESPONSE
                    is NoInternetException -> NO_INTERNET_ERROR
                    is SocketTimeoutException,
                    is UnknownHostException,
                    is SocketException -> CONNECTION_ERROR
                    is IOException -> GENERAL_IO_ERROR
                    else -> UNKNOWN_ERROR
                }
            }
        }
    }
}

fun Throwable.toApiException(): ApiException {
    return ApiException(ApiException.Type.from(this), cause = this)
}
