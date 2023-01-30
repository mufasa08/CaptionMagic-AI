package com.devinjapan.aisocialmediaposter.shared.analytics

expect class AnalyticsTracker {

    fun logClearedRecentList()
    fun logUpdateSelectedTone(toneSelected: String)
    fun logLinkClicked(link: String)
    fun logLogin(userId: String)
    fun logAppOpen()
    fun logAppClose(openedDuration: Long)
    fun logApiCall(apiName: String, parameters: String?)
    fun logApiCallComplete(
        apiName: String,
        parameters: String?,
        httpStatus: Int,
        responseTime: Long
    )

    fun logApiCallError(
        apiName: String,
        parameters: String?,
        errorType: String?,
        responseTime: Long?
    )

    fun logOpenStartupGuideFAQ(url: String)
    fun logOpenContactsFAQ(url: String)
    fun logWalkthroughPageViewed(page: Int)
}
