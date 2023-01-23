package com.devinjapan.aisocialmediaposter.data.repository

import com.devinjapan.aisocialmediaposter.analytics.AnalyticsTracker
import com.devinjapan.aisocialmediaposter.domain.model.AnonymousUser
import com.devinjapan.aisocialmediaposter.domain.repository.AuthRepository
import com.devinjapan.aisocialmediaposter.domain.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val analyticsTracker: AnalyticsTracker
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
