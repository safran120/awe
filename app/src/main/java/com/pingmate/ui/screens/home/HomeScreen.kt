package com.pingmate.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pingmate.ui.theme.PingMateTheme

@Composable
fun HomeScreen() {
    PingMateTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Home Screen", style = MaterialTheme.typography.headlineLarge)
            }
        }
    }
}
