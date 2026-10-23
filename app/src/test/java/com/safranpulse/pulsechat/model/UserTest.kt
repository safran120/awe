package com.safranpulse.pulsechat.model

import org.junit.Assert.assertEquals
import org.junit.Test

class UserTest {
    @Test
    fun noArgConstructorCreatesEmptyUser() {
        val user = User()
        assertEquals("", user.uid)
        assertEquals(emptyList<String>(), user.contacts)
    }
}
