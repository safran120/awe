package com.safranpulse.pulsechat.data

import android.content.Context
import com.safranpulse.pulsechat.data.local.MessageDao
import com.safranpulse.pulsechat.model.Message
import com.safranpulse.pulsechat.util.Clock
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class ChatRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val messageDao: MessageDao,
    private val clock: Clock
) {

    // ---- Messages ----
    fun listenToMessages(chatId: String, limit: Int = 50): Flow<List<Message>> = emptyFlow()

    private suspend fun cacheMessages(chatId: String, messages: List<Message>) {
        // Do nothing
    }

    suspend fun loadMore(chatId: String, beforeTimestamp: Long, limit: Int = 50): List<Message> = emptyList()

    suspend fun sendMessage(
        chatId: String,
        senderId: String,
        content: String,
        type: String = "text",
        replyTo: String? = null,
        groupId: String? = null
    ): String {
        return "dummy-message-id"
    }

    suspend fun updateMessageStatus(messageId: String, chatId: String, status: String) {
        // Do nothing
    }

    suspend fun markAsRead(chatId: String, messageId: String, userId: String) {
        // Do nothing
    }

    suspend fun markChatAsRead(chatId: String, userId: String) {
        // Do nothing
    }

    suspend fun addReaction(chatId: String, messageId: String, emoji: String) {
        // Do nothing
    }

    suspend fun removeReaction(chatId: String, messageId: String, emoji: String) {
        // Do nothing
    }

    suspend fun editMessage(chatId: String, messageId: String, newContent: String) {
        // Do nothing
    }

    suspend fun deleteMessage(chatId: String, messageId: String) {
        // Do nothing
    }

    // ---- Message search within chat ----
    suspend fun searchMessages(chatId: String, query: String): List<Message> = emptyList()

    // ---- Typing indicators ----
    fun listenToTyping(chatId: String): Flow<Map<String, Long>> = emptyFlow()
    suspend fun setTyping(chatId: String, typing: Boolean) {
        // Do nothing
    }

    // ---- Chat list / inbox ----
    fun listenToChats(): Flow<List<ChatSummary>> = emptyFlow()
    suspend fun createDirectChat(otherUid: String): String {
        return "dummy-chat-id"
    }
    suspend fun createGroupChat(
        name: String,
        participantUids: List<String>,
        imageUrl: String = ""
    ): String {
        return "dummy-group-chat-id"
    }
    private suspend fun updateConversationLastMessage(
        chatId: String,
        lastMessage: String,
        timestamp: Long = clock.now()
    ) {
        // Do nothing
    }

    // ---- Media upload ----
    suspend fun uploadImage(chatId: String, uri: android.net.Uri): String {
        return "dummy-image-url"
    }
    suspend fun uploadAudio(chatId: String, uri: android.net.Uri): String {
        return "dummy-audio-url"
    }
}

data class ChatSummary(
    val id: String,
    val name: String,
    val type: String,
    val imageUrl: String,
    val lastMessage: String,
    val updatedAt: Long,
    val participants: List<String>,
    val unreadCount: Int
)

data class Chat(
    val id: String,
    val name: String,
    val type: String,
    val participants: List<String>,
    val imageUrl: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val readBy: Map<String, Long> = emptyMap(),
    val unreadCount: Map<String, Int> = emptyMap()
)