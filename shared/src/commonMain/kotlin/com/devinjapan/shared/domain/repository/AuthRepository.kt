package com.devinjapan.shared.domain.repository

import com.devinjapan.shared.domain.model.AnonymousUser
import com.devinjapan.shared.domain.util.Resource

interface AuthRepository {
    suspend fun signInAnonymously(): Resource<AnonymousUser>
    suspend fun signOut()
    suspend fun getSignedInUserIfExists(): AnonymousUser?
}
