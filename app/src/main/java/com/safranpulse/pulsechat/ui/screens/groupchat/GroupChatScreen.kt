package com.safranpulse.pulsechat.ui.screens.groupchat

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.safranpulse.pulsechat.navigation.Screen
import com.safranpulse.pulsechat.ui.theme.PulseChatTheme

@Composable
fun GroupChatScreen(
    navController: NavHostController,
    participantUids: List<String>
) {
    val viewModel: GroupChatViewModel = hiltViewModel()

    PulseChatTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "Create Group Chat", style = MaterialTheme.typography.headlineLarge)

                OutlinedTextField(
                    value = viewModel.groupName,
                    onValueChange = { viewModel.groupName = it },
                    label = { Text("Group Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = viewModel.imageUrl,
                    onValueChange = { viewModel.imageUrl = it },
                    label = { Text("Group Photo URL (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        viewModel.createGroup(participantUids) { id ->
                            navController.navigate(Screen.Chat.createRoute(id))
                        }
                    },
                    enabled = !viewModel.isCreating,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (viewModel.isCreating) "Creating..." else "Create Group")
                }

                viewModel.error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                if (viewModel.createdChatId != null) {
                    Text("Group created!", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

