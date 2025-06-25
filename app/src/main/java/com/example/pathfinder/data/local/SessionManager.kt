package com.example.pathfinder.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

private val Context.dataStore by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {

    suspend fun loadSessionFromFirestore() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userDoc = FirebaseFirestore.getInstance().collection("users").document(uid)
        val snapshot = userDoc.get().await()

        if (snapshot.exists()) {
            val role = snapshot.getString("role") ?: "candidate"
            val hasProfile = snapshot.getBoolean("hasProfile") ?: false

            saveSession(
                loggedIn = true,
                hasProfile = hasProfile,
                role = role
            )
        }
    }

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val HAS_PROFILE = booleanPreferencesKey("has_profile")
        val ROLE = stringPreferencesKey("role")
    }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_LOGGED_IN] ?: false }

    val hasProfileFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[HAS_PROFILE] ?: false }

    val roleFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[ROLE] ?: "" }

    data class SessionState(
        val isLoggedIn: Boolean,
        val hasProfile: Boolean,
        val role: String // "recruiter" hoặc "candidate"
    )

    val sessionStateFlow: Flow<SessionState> = combine(
        isLoggedInFlow,
        hasProfileFlow,
        roleFlow
    ) { loggedIn, hasProfile, role ->
        SessionState(loggedIn, hasProfile, role)
    }

    suspend fun saveSession(loggedIn: Boolean, hasProfile: Boolean, role: String) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = loggedIn
            preferences[HAS_PROFILE] = hasProfile
            preferences[ROLE] = role
        }
    }

    suspend fun getSession(): SessionState {
        val data = context.dataStore.data.first()
        return SessionState(
            isLoggedIn = data[IS_LOGGED_IN] ?: false,
            hasProfile = data[HAS_PROFILE] ?: false,
            role = data[ROLE] ?: ""
        )
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}

