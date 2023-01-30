package com.devinjapan.aisocialmediaposter.shared.data.source.remote

import com.devinjapan.aisocialmediaposter.shared.domain.model.AnonymousUser
import com.devinjapan.aisocialmediaposter.shared.domain.util.Resource

/** An interface to process the images with different vision detectors and custom image models.  */
actual interface AuthDataSource {
    actual suspend fun signInAnonymously(): Resource<AnonymousUser>
    actual suspend fun signOut()
    actual suspend fun getSignedInUserIfExists(): AnonymousUser?
}
