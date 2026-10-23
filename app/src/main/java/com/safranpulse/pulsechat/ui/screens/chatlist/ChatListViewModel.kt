package com.safranpulse.pulsechat.ui.screens.chatlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.safranpulse.pulsechat.data.ChatRepository
import com.safranpulse.pulsechat.data.ChatSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {
    var chats by mutableStateOf<List<ChatSummary>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    val currentUserId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    init { loadChats() }

    fun loadChats() {
        isLoading = true
        error = null
        viewModelScope.launch {
            repository.listenToChats().collect { list ->
                chats = list
                isLoading = false
            }
        }
    }

    fun markChatAsRead(chatId: String) {
        viewModelScope.launch {
            repository.markChatAsRead(chatId, currentUserId)
        }
    }
}
