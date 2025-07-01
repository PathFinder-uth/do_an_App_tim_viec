package com.example.pathfinder.model
import org.junit.Assert.assertEquals
import org.junit.Test
import com.example.pathfinder.data.model.LoginRequest
class LoginRequestTest {
    @Test
    fun `login request fields test`() {
        val request = LoginRequest(email = "john@example.com", password = "123456")
        assertEquals("john@example.com", request.email)
        assertEquals("123456", request.password)
    }
}