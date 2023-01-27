package com.devinjapan.aisocialmediaposter.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.devinjapan.aisocialmediaposter.R
import com.example.shared.data.error.ApiException

@Composable
fun com.example.shared.data.error.ApiException.toUserUnderstandableMessage(): String {
    val context = LocalContext.current
    return when (this.type) {
        com.example.shared.data.error.ApiException.Type.NO_INTERNET_ERROR -> context.getString(R.string.offline)
        com.example.shared.data.error.ApiException.Type.BAD_REQUEST -> context.getString(R.string.exception_message_bad_request)
        com.example.shared.data.error.ApiException.Type.UNAUTHORIZED -> context.getString(R.string.exception_message_not_allowed)
        com.example.shared.data.error.ApiException.Type.FORBIDDEN -> context.getString(R.string.exception_message_something_went_wrong)
        com.example.shared.data.error.ApiException.Type.CONFLICT -> context.getString(R.string.exception_message_something_went_wrong)
        com.example.shared.data.error.ApiException.Type.INVALID_RESPONSE -> context.getString(R.string.exception_message_something_went_wrong)
        com.example.shared.data.error.ApiException.Type.CONNECTION_ERROR -> context.getString(R.string.exception_message_try_again_later)
        com.example.shared.data.error.ApiException.Type.GENERAL_IO_ERROR -> context.getString(R.string.exception_message_something_went_wrong)
        com.example.shared.data.error.ApiException.Type.UNKNOWN_ERROR -> context.getString(R.string.exception_message_something_went_wrong)
    }
}
