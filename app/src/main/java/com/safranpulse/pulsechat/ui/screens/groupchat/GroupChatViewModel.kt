package com.safranpulse.pulsechat.ui.screens.groupchat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safranpulse.pulsechat.data.ChatRepository
import com.safranpulse.pulsechat.data.Chat
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class GroupChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {
    var groupName by mutableStateOf("")
    var imageUrl by mutableStateOf("")
    var isCreating by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var createdChatId by mutableStateOf<String?>(null)

    fun createGroup(participantUids: List<String>, onCreated: (String) -> Unit) {
        if (groupName.isBlank()) {
            error = "Group name is required"
            return
        }
        isCreating = true
        error = null
        viewModelScope.launch {
            val id = repository.createGroupChat(groupName.trim(), participantUids, imageUrl)
            createdChatId = id
            isCreating = false
            onCreated(id)
        }
    }
}
