package com.safranpulse.pulsechat.ui.screens.auth

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safranpulse.pulsechat.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var displayName by mutableStateOf("")
    var bio by mutableStateOf("")
    var avatarUri by mutableStateOf<Uri?>(null)
    var isSignUp by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun login(onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            error = "Email and password are required"
            return
        }
        isLoading = true
        error = null
        viewModelScope.launch {
            repository.signIn(email, password)
                .onSuccess { onSuccess() }
                .onFailure { error = it.message ?: "Login failed" }
            isLoading = false
        }
    }

    fun signUp(onSuccess: () -> Unit) {
        if (email.isBlank() || password.length < 6) {
            error = "Valid email and 6+ char password required"
            return
        }
        if (isSignUp && displayName.isBlank()) {
            error = "Display name is required"
            return
        }
        isLoading = true
        error = null
        viewModelScope.launch {
            repository.signUp(email, password, displayName, bio, avatarUri)
                .onSuccess { onSuccess() }
                .onFailure { error = it.message ?: "Sign up failed" }
            isLoading = false
        }
    }
}
