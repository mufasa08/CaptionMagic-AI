package com.devinjapan.aisocialmediaposter.shared.data.repository

import com.devinjapan.aisocialmediaposter.shared.data.source.remote.AuthDataSource
import com.devinjapan.aisocialmediaposter.shared.domain.model.AnonymousUser
import com.devinjapan.aisocialmediaposter.shared.domain.repository.AuthRepository
import com.devinjapan.aisocialmediaposter.shared.domain.util.Resource

class AuthRepositoryImpl(
    private val auth: AuthDataSource,
    private val analyticsTracker: com.devinjapan.aisocialmediaposter.shared.analytics.AnalyticsTracker
) : AuthRepository {

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun getSignedInUserIfExists(): AnonymousUser? {
        return auth.getSignedInUserIfExists()
    }

    override suspend fun signInAnonymously(): Resource<AnonymousUser> {
        val response = auth.signInAnonymously()
        if (response is Resource.Success) {
            response.data?.userId?.let { analyticsTracker.logLogin(it) }
        }
        return response
    }
}
