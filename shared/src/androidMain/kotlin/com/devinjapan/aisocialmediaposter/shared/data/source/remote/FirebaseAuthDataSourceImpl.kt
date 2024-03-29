package com.devinjapan.aisocialmediaposter.shared.data.source.remote

import com.devinjapan.aisocialmediaposter.shared.domain.model.AnonymousUser
import com.devinjapan.aisocialmediaposter.shared.domain.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseAuthDataSourceImpl(private val auth: FirebaseAuth) :
    AuthDataSource {
    override suspend fun signInAnonymously(): Resource<AnonymousUser> {
        try {
            auth.signInAnonymously().await()
            val currentUser =
                auth.currentUser ?: return Resource.Error("Problem signing in.")

            return Resource.Success(currentUser.toAnonymousUser())
        } catch (e: Exception) {
            return Resource.Error(
                message = e.message ?: "Something went wrong signing in.",
                data = null
            )
        }
    }

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
}
