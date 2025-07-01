package com.example.pathfinder.model

import com.example.pathfinder.data.model.User
import org.junit.Assert.*
import org.junit.Test

class UserTest {
    @Test
    fun `create user with default values`() {
        val user = User()

        assertEquals("", user.uid)
        assertNull(user.email)
        assertNull(user.displayName)
    }

    @Test
    fun `create user with custom values`() {
        val user = User(
            uid = "uid123",
            email = "user@example.com",
            displayName = "Test User"
        )

        assertEquals("uid123", user.uid)
        assertEquals("user@example.com", user.email)
        assertEquals("Test User", user.displayName)
    }
}