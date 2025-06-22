package com.example.pathfinder.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val HAS_PROFILE = booleanPreferencesKey("has_profile")
    }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_LOGGED_IN] ?: false }

    val hasProfileFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[HAS_PROFILE] ?: false }

    suspend fun saveSession(loggedIn: Boolean, hasProfile: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = loggedIn
            preferences[HAS_PROFILE] = hasProfile
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}
