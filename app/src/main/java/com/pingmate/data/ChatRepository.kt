package com.pingmate.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pingmate.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepository {
    private val db = Firebase.firestore

    fun listenToMessages(chatId: String): Flow<List<Message>> {
        // Implementation for Firestore Snapshot Listener
        return TODO("Implement SnapshotListener for messages")
    }

    suspend fun sendMessage(chatId: String, senderId: String, content: String, type: String = "text") {
        val msg = Message(chatId = chatId, senderId = senderId, content = content, type = type)
        db.collection("conversations").document(chatId).collection("messages").add(msg)
    }
}
