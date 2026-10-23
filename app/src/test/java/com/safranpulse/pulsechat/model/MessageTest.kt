package com.safranpulse.pulsechat.model

import org.junit.Assert.assertEquals
import org.junit.Test

class MessageTest {
    @Test
    fun defaultTypeIsText() {
        val message = Message()
        assertEquals("text", message.type)
    }

    @Test
    fun imageMessageKeepsType() {
        val message = Message(type = "image", content = "uri")
        assertEquals("image", message.type)
        assertEquals("uri", message.content)
    }
}
