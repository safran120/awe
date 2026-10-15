package com.pingmate.model

import java.util.*

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val chatId: String = "",
    val senderId: String = "",
    val content: String = "",
    val type: String = "text", // text, image, audio, system
    val timestamp: Long = System.currentTimeMillis(),
    val reactions: Map<String, Int> = emptyMap(),
    val readBy: List<String> = emptyList()
)
