package com.example.session.datasource

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session_prefs")

class SessionLocalDataSource(private val context: Context) {

    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")

    suspend fun getUserEmail(): String? {
        return context.dataStore.data.map { preferences ->
            preferences[USER_EMAIL_KEY]
        }.first()
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun saveSession(email: String, token: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_EMAIL_KEY] = email
            prefs[AUTH_TOKEN_KEY] = token
        }
    }
}
