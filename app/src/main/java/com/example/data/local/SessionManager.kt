package com.example.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "ihya_session_prefs")

class SessionManager(private val context: Context) {
    private val keyCurrentEmail = stringPreferencesKey("current_user_email")

    val currentUserEmail: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[keyCurrentEmail]
    }

    suspend fun saveSession(email: String) {
        context.dataStore.edit { prefs ->
            prefs[keyCurrentEmail] = email.trim().lowercase()
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.remove(keyCurrentEmail)
        }
    }
}
