package com.example.session.repository

import com.example.domain.model.SessionInfo
import com.example.domain.repository.SessionRepository
import com.example.session.datasource.SessionLocalDataSource
import com.example.session.datasource.SessionRemoteDataSource

class SessionRepositoryImpl(
    private val localDataSource: SessionLocalDataSource,
    private val remoteDataSource: SessionRemoteDataSource,
    private val appVersion: String,
    private val appFlavor: String
) : SessionRepository {

    override suspend fun getSessionInfo(): SessionInfo {
        val firebaseEmail = remoteDataSource.currentUser
        val displayEmail = firebaseEmail ?: localDataSource.getUserEmail()
        return SessionInfo(
            appVersion = appVersion,
            environment = appFlavor,
            userEmail = displayEmail ?: "No email found"
        )
    }

    override suspend fun logout() {
        remoteDataSource.logout()
        localDataSource.clearSession()
    }
}
