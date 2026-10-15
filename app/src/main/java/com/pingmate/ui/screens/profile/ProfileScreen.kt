package com.pingmate.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pingmate.ui.theme.PingMateTheme

@Composable
fun ProfileScreen() {
    PingMateTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Profile Settings", style = MaterialTheme.typography.headlineLarge)
                
                Text("Avatar", style = MaterialTheme.typography.bodyLarge)
                Button(onClick = { /* TODO */ }) {
                    Text("Update Photo")
                }

                Divider()

                Text("Storage Management", style = MaterialTheme.typography.headlineSmall)
                Text("Current Usage: 450MB / 1GB")
                
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Checkbox(checked = false, onCheckedChange = null)
                    Text("Clear Cache")
                }
                
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Purge Old Messages")
                }
            }
        }
    }
}
