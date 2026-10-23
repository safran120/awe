package com.safranpulse.pulsechat.ui.screens.profile

import android.net.Uri
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

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    var user by mutableStateOf<User?>(null)
    var displayName by mutableStateOf("")
    var bio by mutableStateOf("")
    var avatarUri by mutableStateOf<Uri?>(null)
    var avatarUrl by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var saved by mutableStateOf(false)

    init { load() }

    fun load() {
        isLoading = true
        viewModelScope.launch {
            repository.getCurrentUser()
                .let { loaded ->
                    user = loaded
                    displayName = loaded?.displayName.orEmpty()
                    bio = loaded?.bio.orEmpty()
                    avatarUrl = loaded?.avatarUrl.orEmpty()
                }
            isLoading = false
        }
    }

    fun save(onSuccess: () -> Unit = {}) {
        isLoading = true
        error = null
        saved = false
        viewModelScope.launch {
            repository.updateProfile(displayName, bio, avatarUri)
                .onSuccess {
                    saved = true
                    avatarUri = null
                    load()
                    onSuccess()
                }
                .onFailure { error = it.message ?: "Update failed" }
            isLoading = false
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        repository.signOut()
        onLoggedOut()
    }
}
