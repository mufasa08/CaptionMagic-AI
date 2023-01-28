package com.devinjapan.shared.data.repository

import com.devinjapan.shared.analytics.AnalyticsTracker
import com.devinjapan.shared.data.source.remote.AuthDataSource
import com.devinjapan.shared.domain.model.AnonymousUser
import com.devinjapan.shared.domain.repository.AuthRepository
import com.devinjapan.shared.domain.util.Resource

class AuthRepositoryImpl(
    private val auth: AuthDataSource,
    private val analyticsTracker: AnalyticsTracker
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
