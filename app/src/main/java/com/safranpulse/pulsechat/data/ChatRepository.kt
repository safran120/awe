package com.safranpulse.pulsechat.data

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.safranpulse.pulsechat.data.local.AppDatabase
import com.safranpulse.pulsechat.data.local.MessageDao
import com.safranpulse.pulsechat.data.local.MessageEntity
import com.safranpulse.pulsechat.model.Message
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChatRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val messageDao: MessageDao
) {
    private val db = Firebase.firestore

    private val storage = FirebaseStorage.getInstance()

    private val database: AppDatabase by lazy { AppDatabase.get(context) }

    val currentUserId: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid

    // ---- Messages ----

    fun listenToMessages(chatId: String, limit: Int = 50): Flow<List<Message>> = callbackFlow {
        val registration = db.collection("conversations")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents
                    ?.mapNotNull { doc ->
                        doc.toObject(Message::class.java)?.copy(id = doc.id)
                    }
                    ?.sortedBy { it.timestamp }
                    .orEmpty()
                GlobalScope.launch { cacheMessages(chatId, messages) }
                trySend(messages)
            }
        awaitClose { registration.remove() }
    }

    private suspend fun cacheMessages(chatId: String, messages: List<Message>) {
        database.messageDao().upsert(
            messages.map { msg ->
                MessageEntity(
                    id = msg.id,
                    chatId = msg.chatId,
                    senderId = msg.senderId,
                    content = msg.content,
                    type = msg.type,
                    timestamp = msg.timestamp
                )
            }
        )
    }

    suspend fun loadMore(chatId: String, beforeTimestamp: Long, limit: Int = 50): List<Message> {
        val snapshot = db.collection("conversations")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .startAfter(beforeTimestamp)
            .limit(limit.toLong())
            .get()
            .await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Message::class.java)?.copy(id = doc.id)
        }.sortedBy { it.timestamp }
    }

    suspend fun sendMessage(
        chatId: String,
        senderId: String,
        content: String,
        type: String = "text",
        replyTo: String? = null,
        groupId: String? = null
    ): String {
        val msg = Message(
            chatId = chatId,
            senderId = senderId,
            content = content,
            type = type,
            status = "sending",
            replyTo = replyTo,
            groupId = groupId
        )
        val ref = db.collection("conversations").document(chatId).collection("messages").add(msg).await()
        db.collection("conversations").document(chatId).collection("messages")
            .document(ref.id).update("status", "sent", "id", ref.id).await()
        updateConversationLastMessage(chatId, content, timestamp = System.currentTimeMillis())
        return ref.id
    }

    suspend fun updateMessageStatus(messageId: String, chatId: String, status: String) {
        db.collection("conversations").document(chatId).collection("messages").document(messageId)
            .update("status", status).await()
    }

    suspend fun markAsRead(chatId: String, messageId: String, userId: String) {
        db.collection("conversations").document(chatId).collection("messages").document(messageId)
            .update("readBy", FieldValue.arrayUnion(userId), "status", "read").await()
    }

    suspend fun markChatAsRead(chatId: String, userId: String) {
        db.collection("conversations").document(chatId)
            .update("readBy", FieldValue.arrayUnion(userId)).await()
    }

    suspend fun addReaction(chatId: String, messageId: String, emoji: String) {
        db.collection("conversations").document(chatId).collection("messages").document(messageId)
            .update("reactions.$emoji", FieldValue.increment(1)).await()
    }

    suspend fun removeReaction(chatId: String, messageId: String, emoji: String) {
        db.collection("conversations").document(chatId).collection("messages").document(messageId)
            .update("reactions.$emoji", FieldValue.increment(-1)).await()
    }

    suspend fun editMessage(chatId: String, messageId: String, newContent: String) {
        db.collection("conversations").document(chatId).collection("messages").document(messageId)
            .update("content", newContent, "editedAt", System.currentTimeMillis()).await()
    }

    suspend fun deleteMessage(chatId: String, messageId: String) {
        db.collection("conversations").document(chatId).collection("messages").document(messageId)
            .delete().await()
    }

    // ---- Message search within chat ----

    suspend fun searchMessages(chatId: String, query: String): List<Message> {
        if (query.isBlank()) return emptyList()
        val snapshot = db.collection("conversations")
            .document(chatId)
            .collection("messages")
            .whereGreaterThanOrEqual("content", query)
            .whereLessThanOrEqual("content", query + "\uf7ff")
            .orderBy("content")
            .get()
            .await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Message::class.java)?.copy(id = doc.id)
        }.sortedBy { it.timestamp }
    }

    // ---- Typing indicators ----

    fun listenToTyping(chatId: String): Flow<Map<String, Long>> = callbackFlow {
        val registration = db.collection("conversations").document(chatId)
            .collection("typing")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val now = System.currentTimeMillis()
                val typing = snapshot?.documents
                    ?.associate { doc ->
                        doc.id to (doc.getLong("ts") ?: 0L)
                    }
                    ?.filter { (_, ts) -> now - ts < 5000 }
                    .orEmpty()
                trySend(typing)
            }
        awaitClose { registration.remove() }
    }

    suspend fun setTyping(chatId: String, typing: Boolean) {
        val uid = currentUserId ?: return
        val ref = db.collection("conversations").document(chatId).collection("typing").document(uid)
        if (typing) {
            ref.set(mapOf("ts" to System.currentTimeMillis())).await()
        } else {
            ref.delete().await()
        }
    }

    // ---- Chat list / inbox ----

    fun listenToChats(): Flow<List<ChatSummary>> = callbackFlow {
        val uid = currentUserId
        if (uid == null) {
            close()
            return@callbackFlow
        }
        val registration = db.collection("conversations")
            .whereArrayContains("participants", uid)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val chats = snapshot?.documents
                    ?.mapNotNull { doc ->
                        val data = doc.data
                        val participants = data["participants"] as? List<*> ?: emptyList()
                        val lastMsg = data["lastMessage"] as? String ?: ""
                        val updatedAt = data["updatedAt"] as? Long ?: 0L
                        val unread = data["unreadCount"] as? Map<*, *> ?: emptyMap()
                        val unreadCount = (unread[uid] as? Int) ?: 0
                        ChatSummary(
                            id = doc.id,
                            name = data["name"] as? String ?: "",
                            type = data["type"] as? String ?: "direct",
                            imageUrl = data["imageUrl"] as? String ?: "",
                            lastMessage = lastMsg,
                            updatedAt = updatedAt,
                            participants = participants.filterIsInstance<String>(),
                            unreadCount = unreadCount
                        )
                    }
                    .orEmpty()
                trySend(chats)
            }
        awaitClose { registration.remove() }
    }

    suspend fun createDirectChat(otherUid: String): String {
        val uid = currentUserId ?: throw IllegalStateException("Not signed in")
        val participants = listOf(uid, otherUid)
        val doc = db.collection("conversations").document()
        val chat = Chat(
            id = doc.id,
            name = "",
            type = "direct",
            participants = participants,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            readBy = mapOf(uid to 0L, otherUid to 0L),
            unreadCount = mapOf(uid to 0, otherUid to 0)
        )
        doc.set(chat).await()
        return doc.id
    }

    suspend fun createGroupChat(name: String, participantUids: List<String>, imageUrl: String = ""): String {
        val uid = currentUserId ?: throw IllegalStateException("Not signed in")
        val allParticipants = (participantUids + uid).distinct()
        val doc = db.collection("conversations").document()
        val now = System.currentTimeMillis()
        val chat = Chat(
            id = doc.id,
            name = name,
            type = "group",
            participants = allParticipants,
            imageUrl = imageUrl,
            createdAt = now,
            updatedAt = now,
            readBy = allParticipants.associateWith { 0L }.toMap(),
            unreadCount = allParticipants.associateWith { 0 }.toMap()
        )
        doc.set(chat).await()
        return doc.id
    }

    private suspend fun updateConversationLastMessage(
        chatId: String,
        lastMessage: String,
        timestamp: Long = System.currentTimeMillis()
    ) {
        db.collection("conversations").document(chatId).update(
            "lastMessage" to lastMessage,
            "updatedAt" to timestamp
        ).await()
    }

    // ---- Media upload ----

    suspend fun uploadImage(chatId: String, uri: android.net.Uri): String {
        val ref = storage.reference.child("chats/$chatId/${java.util.UUID.randomUUID()}.webp")
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun uploadAudio(chatId: String, uri: android.net.Uri): String {
        val ref = storage.reference.child("chats/$chatId/audio/${java.util.UUID.randomUUID()}.m4a")
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
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
