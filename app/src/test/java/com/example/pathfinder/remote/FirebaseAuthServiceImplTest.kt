package com.example.pathfinder.remote

import com.example.pathfinder.data.model.LoginRequest
import com.example.pathfinder.data.model.User
import com.example.pathfinder.data.remote.IAuthService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FirebaseAuthServiceImplTest {
    private lateinit var firebaseService: IAuthService

    @Before
    fun setup() {
        firebaseService = FakeFirebaseAuthService()
    }

    @Test
    fun `login success returns user`() = runBlocking {
        val request = LoginRequest("success@example.com", "123456")
        val result = firebaseService.loginWithEmail(request)
        assertTrue(result.isSuccess)
        assertEquals("success@example.com", result.getOrNull()?.email)
    }

    @Test
    fun `login failure returns error`() = runBlocking {
        val request = LoginRequest("fail@example.com", "wrongpass")
        val result = firebaseService.loginWithEmail(request)
        assertTrue(result.isFailure)
    }

    class FakeFirebaseAuthService : IAuthService {
        override suspend fun loginWithEmail(request: LoginRequest): Result<User> {
            return if (request.email == "success@example.com") {
                Result.success(User("id123", request.email, "FakeUser"))
            } else {
                Result.failure(Exception("Invalid credentials"))
            }
        }

        override fun getCurrentUser(): User? = null
        override fun logout() {}
    }
}
