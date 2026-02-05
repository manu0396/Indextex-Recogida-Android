package com.example.domain.repository

import com.example.domain.model.SessionInfo

interface SessionRepository {
    suspend fun getSessionInfo(): SessionInfo
    suspend fun logout()
}
