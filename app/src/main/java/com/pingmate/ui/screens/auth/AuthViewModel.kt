package com.pingmate.ui.screens.auth

import androidx.lifecycle.ViewModel
import com.pingmate.data.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {
    fun signUp(email: String, pass: String, name: String, avatar: String) {
        // launch coroutine to call repository.signUp
    }
}
