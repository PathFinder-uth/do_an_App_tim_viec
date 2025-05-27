package com.example.pathfinder.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.example.pathfinder.data.model.LoginRequest
import com.example.pathfinder.data.model.User
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FacebookAuthProvider


 class FirebaseAuthServiceImpl(private val auth: FirebaseAuth = FirebaseAuth.getInstance()):IAuthService {

     override suspend fun loginWithEmail(loginRequest: LoginRequest): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(loginRequest.email, loginRequest.password).await()
            val firebaseUser = result.user
            firebaseUser?.let {
                Result.success(
                    User(
                        uid = it.uid,
                        email = it.email,
                        displayName = it.displayName
                    )
                )
            } ?: Result.failure(Exception("User is null"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
     override suspend fun loginWithGoogle(idToken: String): Result<User> {
         return try {
             val credential = GoogleAuthProvider.getCredential(idToken, null)
             val result = auth.signInWithCredential(credential).await()
             val firebaseUser = result.user
             firebaseUser?.let {
                 Result.success(User(uid = it.uid, email = it.email, displayName = it.displayName))
             } ?: Result.failure(Exception("Google user is null"))
         } catch (e: Exception) {
             Result.failure(e)
         }
     }

     override suspend fun loginWithFacebook(token: String): Result<User> {
         return try {
             val credential = FacebookAuthProvider.getCredential(token)
             val result = auth.signInWithCredential(credential).await()
             val firebaseUser = result.user
             firebaseUser?.let {
                 Result.success(User(uid = it.uid, email = it.email, displayName = it.displayName))
             } ?: Result.failure(Exception("Facebook user is null"))
         } catch (e: Exception) {
             Result.failure(e)
         }
     }
   override fun getCurrentUser(): User? {
        val user = auth.currentUser
        return user?.let {
            User(
                uid = it.uid,
                email = it.email,
                displayName = it.displayName
            )
        }
    }

   override fun logout() {
        auth.signOut()
    }
}