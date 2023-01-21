package com.devinjapan.aisocialmediaposter.domain.repository

import com.devinjapan.aisocialmediaposter.domain.model.AnonymousUser
import com.devinjapan.aisocialmediaposter.domain.util.Resource

interface AuthRepository {
    suspend fun signInAnonymously(): Resource<AnonymousUser>
    suspend fun signOut()
    suspend fun getSignedInUserIfExists(): AnonymousUser?
}
