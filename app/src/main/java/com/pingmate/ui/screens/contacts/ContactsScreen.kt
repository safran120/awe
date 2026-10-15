package com.pingmate.ui.screens.contacts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pingmate.ui.theme.PingMateTheme

@Composable
fun ContactsScreen() {
    PingMateTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(text = "Contacts", style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(16.dp))
                
                // Search Bar
                TextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search by name or email") }
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(10) { index ->
                        ListItem(
                            headlineContent = { Text("User $index") },
                            supportingContent = { Text("user$index@example.com") }
                        )
                    }
                }
            }
        }
    }
}
