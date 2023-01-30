package com.devinjapan.aisocialmediaposter.shared.domain.repository

import com.devinjapan.aisocialmediaposter.shared.domain.model.AnonymousUser
import com.devinjapan.aisocialmediaposter.shared.domain.util.Resource

interface AuthRepository {
    suspend fun signInAnonymously(): Resource<AnonymousUser>
    suspend fun signOut()
    suspend fun getSignedInUserIfExists(): AnonymousUser?
}
