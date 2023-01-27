package com.devinjapan.shared.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.ParametersBuilder
import com.google.firebase.analytics.ktx.logEvent

class AnalyticsTracker(val firebaseAnalytics: FirebaseAnalytics) {

    /*private fun updateDefaultParameters() {
        firebaseAnalytics.setDefaultEventParameters(Bundle().also { bundle ->
            bundle.putString("####", ####)
        })
    }*/

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

    fun logClearedRecentList() {
        logEvent("cleared_recent_list")
    }

    fun logUpdateSelectedTone(toneSelected: String) {
        logEvent("update_selected_tone") {
            stringParam("tone_selected", toneSelected)
        }
    }

    fun logLinkClicked(link: String) {
        logEvent("link_clicked") {
            stringParam("link", link)
        }
    }

    fun logLogin(userId: String) {
        logEvent("login")
        firebaseAnalytics.setUserId(userId)
    }

    fun logAppOpen() {
        logEvent("app_open")
    }

    fun logAppClose(openedDuration: Long) {
        logEvent("app_close") {
            longParam("opened_duration", openedDuration)
        }
    }

    fun logApiCall(apiName: String, parameters: String?) {
        logEvent("api_call") {
            stringParam("type", apiName)
            stringParam("parameters", parameters)
        }
    }

    fun logApiCallComplete(
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

    fun logApiCallError(
        apiName: String,
        parameters: String?,
        errorType: String? = null,
        responseTime: Long? = null
    ) {
        logEvent("api_call_error") {
            stringParam("type", apiName)
            stringParam("parameters", parameters)
            stringParam("error_type", errorType)
            longParam("response_time", responseTime)
        }
    }

    fun logOpenStartupGuideFAQ(url: String) {
        logEvent("open_startup_guide") {
            stringParam("url", url)
        }
    }

    fun logOpenContactsFAQ(url: String) {
        logEvent("open_faq") {
            stringParam("url", url)
        }
    }

    fun logWalkthroughPageViewed(page: Int) {
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
                    value.trimByLength(com.example.shared.AnalyticsTracker.MAX_LENGTH_EVENT_PARAMETER_VALUE)
                )
            }
        }
    }
}
