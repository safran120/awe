package com.safranpulse.pulsechat.ui.screens.chat

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.safranpulse.pulsechat.data.ChatRepository
import com.safranpulse.pulsechat.data.UserRepository
import com.safranpulse.pulsechat.model.Message
import com.safranpulse.pulsechat.util.ImageCompressor
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlin.Result

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _typing = MutableStateFlow<Map<String, Long>>(emptyMap())
    val typing: StateFlow<Map<String, Long>> = _typing.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Message>>(emptyList())
    val searchResults: StateFlow<List<Message>> = _searchResults.asStateFlow()
    private val _isSearching = MutableStateFlow(false)
    val isSearchingField: StateFlow<Boolean> = _isSearching.asStateFlow()
    private var isSearching = false
        set(value) {
            field = value
            _isSearching.value = value
        }

    private var chatId: String = ""
    private val pageSize = 50

    val currentUserId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    private var blockedUids = emptySet<String>()

    fun start(chatId: String) {
        this.chatId = chatId
        viewModelScope.launch {
            userRepository.getBlockedUsers()
                .onSuccess { users -> blockedUids = users.map { it.uid }.toSet() }
            repository.listenToMessages(chatId, pageSize).collect { list ->
                val filtered = list.filter { msg ->
                    msg.senderId !in blockedUids && msg.id.isNotBlank()
                }
                _messages.value = filtered
                val uid = currentUserId
                filtered.filter { it.senderId != uid && !it.readBy.contains(uid) }
                    .forEach { repository.markAsRead(chatId, it.id, uid) }
            }
            repository.listenToTyping(chatId).collect { typingMap ->
                _typing.value = typingMap
            }
        }
    }

    fun loadMore() {
        val oldest = _messages.value.minByOrNull { it.timestamp }?.timestamp ?: return
        viewModelScope.launch {
            val older = repository.loadMore(chatId, oldest, pageSize)
            _messages.value = (older + _messages.value)
                .distinctBy { it.id }
                .sortedBy { it.timestamp }
        }
    }

    fun sendText(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val id = repository.sendMessage(chatId, currentUserId, text, "text")
            // simulate delivery
            delay(1000)
            repository.updateMessageStatus(id, chatId, "delivered")
            delay(1000)
            repository.updateMessageStatus(id, chatId, "read")
        }
    }

    fun sendMedia(uriString: String, type: String) {
        viewModelScope.launch {
            val uri = Uri.parse(uriString)
            val payload = if (type == "image") {
                ImageCompressor.compress(context, uri)?.toString() ?: uriString
            } else {
                uriString
            }
            val url = if (type == "image") repository.uploadImage(chatId, Uri.parse(payload)) else payload
            if (type == "audio") {
                repository.uploadAudio(chatId, uri).let { audioUrl ->
                    repository.sendMessage(chatId, currentUserId, audioUrl, "audio")
                }
            } else {
                repository.sendMessage(chatId, currentUserId, url, type)
            }
        }
    }

    fun react(messageId: String, emoji: String) {
        viewModelScope.launch {
            if (messageId.isNotBlank()) repository.addReaction(chatId, messageId, emoji)
        }
    }

    fun unreact(messageId: String, emoji: String) {
        viewModelScope.launch {
            if (messageId.isNotBlank()) repository.removeReaction(chatId, messageId, emoji)
        }
    }

    fun setTyping(typing: Boolean) {
        viewModelScope.launch {
            repository.setTyping(chatId, typing)
        }
    }

    fun searchMessages(query: String) {
        isSearching = query.isNotBlank()
        if (!isSearching) {
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {
            val results = repository.searchMessages(chatId, query)
            _searchResults.value = results
        }
    }

    fun editMessage(messageId: String, newContent: String) {
        viewModelScope.launch {
            repository.editMessage(chatId, messageId, newContent)
        }
    }

    fun deleteMessage(messageId: String) {
        viewModelScope.launch {
            repository.deleteMessage(chatId, messageId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch { repository.setTyping(chatId, false) }
    }
}
