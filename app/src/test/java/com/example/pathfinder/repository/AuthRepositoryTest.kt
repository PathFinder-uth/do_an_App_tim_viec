package com.example.pathfinder.repository

import com.example.pathfinder.data.model.LoginRequest
import com.example.pathfinder.data.model.User
import com.example.pathfinder.data.remote.FirebaseAuthServiceImpl
import com.example.pathfinder.data.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.*
@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryTest {
    private lateinit var authService: FirebaseAuthServiceImpl
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        authService = mock(FirebaseAuthServiceImpl::class.java)
        authRepository = AuthRepository(authService)
    }

    @Test
    fun `login success returns user`() = runTest {
        val request = LoginRequest("test@example.com", "123456")
        val user = User("123", "Test User", "test@example.com")
        whenever(authService.loginWithEmail(request)).thenReturn(Result.success(user))

        val result = authRepository.loginWithEmail(request)

        assertTrue(result.isSuccess)
        assertEquals(user, result.getOrNull())
    }

    @Test
    fun `login failure returns error`() = runTest {
        val request = LoginRequest("bad@example.com", "wrongpass")
        whenever(authService.loginWithEmail(request)).thenReturn(Result.failure(Exception("Auth failed")))

        val result = authRepository.loginWithEmail(request)

        assertTrue(result.isFailure)
    }

}