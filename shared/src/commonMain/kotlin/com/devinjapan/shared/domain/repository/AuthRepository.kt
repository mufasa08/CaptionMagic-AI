package com.devinjapan.shared.domain.repository

import com.example.shared.domain.model.AnonymousUser
import com.example.shared.domain.util.Resource

interface AuthRepository {
    suspend fun signInAnonymously(): Resource<AnonymousUser>
    suspend fun signOut()
    suspend fun getSignedInUserIfExists(): AnonymousUser?
}
