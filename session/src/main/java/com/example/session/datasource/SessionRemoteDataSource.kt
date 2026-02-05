package com.example.session.datasource

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class SessionRemoteDataSource(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    val currentUser: String?
        get() = firebaseAuth.currentUser?.email

    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    suspend fun logout() {
        firebaseAuth.signOut()
    }

    // Example login (if you need it here, or strictly via UI Auth flows)
    suspend fun refreshSession() {
        firebaseAuth.currentUser?.reload()?.await()
    }
}
