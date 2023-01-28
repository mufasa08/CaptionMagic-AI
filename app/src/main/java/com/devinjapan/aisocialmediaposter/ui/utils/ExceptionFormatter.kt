package com.devinjapan.aisocialmediaposter.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.devinjapan.aisocialmediaposter.R
import com.devinjapan.shared.data.error.ApiException

@Composable
fun ApiException.toUserUnderstandableMessage(): String {
    val context = LocalContext.current
    return when (this.type) {
        ApiException.Type.NO_INTERNET_ERROR -> context.getString(R.string.offline)
        ApiException.Type.BAD_REQUEST -> context.getString(R.string.exception_message_bad_request)
        ApiException.Type.UNAUTHORIZED -> context.getString(R.string.exception_message_not_allowed)
        ApiException.Type.FORBIDDEN -> context.getString(R.string.exception_message_something_went_wrong)
        ApiException.Type.CONFLICT -> context.getString(R.string.exception_message_something_went_wrong)
        ApiException.Type.INVALID_RESPONSE -> context.getString(R.string.exception_message_something_went_wrong)
        ApiException.Type.CONNECTION_ERROR -> context.getString(R.string.exception_message_try_again_later)
        ApiException.Type.GENERAL_IO_ERROR -> context.getString(R.string.exception_message_something_went_wrong)
        ApiException.Type.UNKNOWN_ERROR -> context.getString(R.string.exception_message_something_went_wrong)
    }
}
