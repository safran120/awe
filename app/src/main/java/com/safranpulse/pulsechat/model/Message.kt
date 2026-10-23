package com.safranpulse.pulsechat.model

import java.util.*

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val chatId: String = "",
    val senderId: String = "",
    val content: String = "",
    val type: String = "text",
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "sending",
    val reactions: Map<String, Int> = emptyMap(),
    val readBy: List<String> = emptyList(),
    val replyTo: String? = null,
    val editedAt: Long? = null,
    val groupId: String? = null
) {
    constructor() : this("", "", "", "", "text", System.currentTimeMillis(), "sending", emptyMap(), emptyList(), null, null, null)
}
