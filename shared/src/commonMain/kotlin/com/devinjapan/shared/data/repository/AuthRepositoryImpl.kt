package com.devinjapan.shared.data.repository

import com.devinjapan.shared.domain.model.AnonymousUser
import com.devinjapan.shared.domain.repository.AuthRepository
import com.devinjapan.shared.domain.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val analyticsTracker: com.devinjapan.shared.analytics.AnalyticsTracker
) : AuthRepository {

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun getSignedInUserIfExists(): AnonymousUser? {
        val currentUser =
            auth.currentUser ?: return null
        return currentUser.toAnonymousUser()
    }

    companion object {
        fun FirebaseUser.toAnonymousUser(): AnonymousUser {
            return AnonymousUser(
                userId = this.uid
            )
        }
    }

    override suspend fun signInAnonymously(): Resource<AnonymousUser> {
        try {
            auth.signInAnonymously().await()
            val currentUser =
                auth.currentUser ?: return Resource.Error("Problem signing in.")
            analyticsTracker.logLogin(currentUser.uid)
            return Resource.Success(currentUser.toAnonymousUser())
        } catch (e: Exception) {
            return Resource.Error(
                message = e.message ?: "Something went wrong signing in.",
                data = null
            )
        }
    }
}
