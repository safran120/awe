package com.pingmate.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pingmate.ui.theme.PingMateTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() {
    var email by mutableStateOf(" \)
 var password by mutableStateOf(\\)
 var name by mutableStateOf(\\)
 var isLoading by mutableStateOf(false)

 fun login() {
 // Logic for login
 }

 fun signUp() {
 // Logic for sign up
 }
}

@Composable
fun AuthScreen() {
 PingMateTheme {
 Surface(modifier = Modifier.fillMaxSize()) {
 Column(
 modifier = Modifier.fillMaxSize().padding(16.dp),
 verticalArrangement = Arrangement.spacedBy(16.dp)
 ) {
 Text(text = \PingMate Login\, style = MaterialTheme.typography.headlineLarge)
 
 OutlinedTextField(
 value = email, 
 onValueChange = { email = it }, 
 label = { Text(\Email\) },
 modifier = Modifier.fillMaxWidth()
 )
 
 OutlinedTextField(
 value = password, 
 onValueChange = { password = it }, 
 label = { Text(\Password\) },
 modifier = Modifier.fillMaxWidth()
 )
 
 Button(onClick = { login() }, modifier = Modifier.fillMaxWidth()) {
 Text(\Login\)
 }
 }
 }
 }
}

