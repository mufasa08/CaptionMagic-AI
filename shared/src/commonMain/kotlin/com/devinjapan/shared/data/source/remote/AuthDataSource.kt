package com.devinjapan.shared.data.source.remote

import com.devinjapan.shared.domain.model.AnonymousUser
import com.devinjapan.shared.domain.util.Resource

/** An interface to process the images with different vision detectors and custom image models.  */
expect interface AuthDataSource {
    suspend fun signInAnonymously(): Resource<AnonymousUser>

    suspend fun signOut()

    suspend fun getSignedInUserIfExists(): AnonymousUser?
}
