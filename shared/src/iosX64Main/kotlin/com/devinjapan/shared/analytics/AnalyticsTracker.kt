package com.devinjapan.shared.analytics

actual class AnalyticsTracker {
    actual fun logClearedRecentList() {
    }

    actual fun logUpdateSelectedTone(toneSelected: String) {
    }

    actual fun logLinkClicked(link: String) {
    }

    actual fun logLogin(userId: String) {
    }

    actual fun logAppOpen() {
    }

    actual fun logAppClose(openedDuration: Long) {
    }

    actual fun logApiCall(apiName: String, parameters: String?) {
    }

    actual fun logApiCallComplete(
        apiName: String,
        parameters: String?,
        httpStatus: Int,
        responseTime: Long
    ) {
    }

    actual fun logApiCallError(
        apiName: String,
        parameters: String?,
        errorType: String?,
        responseTime: Long?
    ) {
    }

    actual fun logOpenStartupGuideFAQ(url: String) {
    }

    actual fun logOpenContactsFAQ(url: String) {
    }

    actual fun logWalkthroughPageViewed(page: Int) {
    }
}
