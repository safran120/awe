package com.safranpulse.pulsechat.ui.screens.contacts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safranpulse.pulsechat.data.UserRepository
import com.safranpulse.pulsechat.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlin.Result

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    var query by mutableStateOf("")
    var searchResults by mutableStateOf<List<User>>(emptyList())
    var contacts by mutableStateOf<List<User>>(emptyList())
    var blockedUsers by mutableStateOf<List<User>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    init { loadContacts(); loadBlocked() }

    fun search() {
        isLoading = true
        error = null
        viewModelScope.launch {
            repository.searchUsers(query)
                .onSuccess { searchResults = it }
                .onFailure { error = it.message ?: "Search failed" }
            isLoading = false
        }
    }

    fun addContact(uid: String) {
        viewModelScope.launch {
            repository.addContact(uid)
                .onSuccess { loadContacts() }
                .onFailure { error = it.message ?: "Add failed" }
        }
    }

    fun removeContact(uid: String) {
        viewModelScope.launch {
            repository.removeContact(uid)
                .onSuccess { loadContacts() }
                .onFailure { error = it.message ?: "Remove failed" }
        }
    }

    fun blockUser(uid: String) {
        viewModelScope.launch {
            repository.blockUser(uid)
                .onSuccess { loadBlocked() }
                .onFailure { error = it.message ?: "Block failed" }
        }
    }

    fun unblockUser(uid: String) {
        viewModelScope.launch {
            repository.unblockUser(uid)
                .onSuccess { loadBlocked() }
                .onFailure { error = it.message ?: "Unblock failed" }
        }
    }

    fun loadContacts() {
        isLoading = true
        viewModelScope.launch {
            repository.getContacts()
                .onSuccess { contacts = it }
                .onFailure { error = it.message ?: "Load failed" }
            isLoading = false
        }
    }

    fun loadBlocked() {
        viewModelScope.launch {
            repository.getBlockedUsers()
                .onSuccess { blockedUsers = it }
                .onFailure { error = it.message ?: "Load blocked failed" }
        }
    }
}
