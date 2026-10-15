package com.pingmate.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pingmate.data.ChatRepository
import com.pingmate.model.Message
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {
    fun sendMessage(chatId: String, senderId: String, content: String) {
        viewModelScope.launch {
            repository.sendMessage(chatId, senderId, content)
        }
    }
}
