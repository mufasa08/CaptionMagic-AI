package com.devinjapan.aisocialmediaposter.shared.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.ParametersBuilder
import com.google.firebase.analytics.ktx.logEvent

actual class AnalyticsTracker(private val firebaseAnalytics: FirebaseAnalytics) {

    private fun logEvent(name: String, params: (ParametersBuilder.() -> Unit)? = null) {
        firebaseAnalytics.logEvent(name) {
            params?.invoke(this)
        }
    }

    fun logEvent(name: String, params: Bundle?) {
        firebaseAnalytics.logEvent(name) {
            params?.let { bundle.putAll(it) }
        }
    }

    actual fun logClearedRecentList() {
        logEvent("cleared_recent_list")
    }

    actual fun logUpdateSelectedTone(toneSelected: String) {
        logEvent("update_selected_tone") {
            stringParam("tone_selected", toneSelected)
        }
    }

    actual fun logLinkClicked(link: String) {
        logEvent("link_clicked") {
            stringParam("link", link)
        }
    }

    actual fun logLogin(userId: String) {
        logEvent("login")
        firebaseAnalytics.setUserId(userId)
    }

    actual fun logAppOpen() {
        logEvent("app_open")
    }

    actual fun logAppClose(openedDuration: Long) {
        logEvent("app_close") {
            longParam("opened_duration", openedDuration)
        }
    }

    actual fun logApiCall(apiName: String, parameters: String?) {
        logEvent("api_call") {
            stringParam("type", apiName)
            stringParam("parameters", parameters)
        }
    }

    actual fun logApiCallComplete(
        apiName: String,
        parameters: String?,
        httpStatus: Int,
        responseTime: Long
    ) {
        logEvent("api_call_complete") {
            stringParam("type", apiName)
            stringParam("parameters", parameters)
            intParam("http_status", httpStatus)
            longParam("response_time", responseTime)
        }
    }

    actual fun logApiCallError(
        apiName: String,
        parameters: String?,
        errorType: String?,
        responseTime: Long?
    ) {
        logEvent("api_call_error") {
            stringParam("type", apiName)
            stringParam("parameters", parameters)
            stringParam("error_type", errorType)
            longParam("response_time", responseTime)
        }
    }

    actual fun logOpenStartupGuideFAQ(url: String) {
        logEvent("open_startup_guide") {
            stringParam("url", url)
        }
    }

    actual fun logOpenContactsFAQ(url: String) {
        logEvent("open_faq") {
            stringParam("url", url)
        }
    }

    actual fun logWalkthroughPageViewed(page: Int) {
        logEvent("walthrough") {
            intParam("page_viewed", page)
        }
    }

    //endregion

    companion object {
        // https://support.google.com/firebase/answer/9237506
        private const val MAX_LENGTH_EVENT_PARAMETER_VALUE = 100
        private const val MAX_LENGTH_USER_PROPERTY_VALUE = 36

        private fun String.trimByLength(maxLength: Int): String {
            return if (length > maxLength) {
                substring(0, maxLength)
            } else {
                this
            }
        }

        private fun ParametersBuilder.intParam(key: String, value: Int?) {
            if (value != null) {
                param(key, value.toLong())
            }
        }

        private fun ParametersBuilder.longParam(key: String, value: Long?) {
            if (value != null) {
                param(key, value)
            }
        }

        private fun ParametersBuilder.stringParam(key: String, value: String?) {
            if (value != null) {
                param(
                    key,
                    value.trimByLength(MAX_LENGTH_EVENT_PARAMETER_VALUE)
                )
            }
        }
    }
}
