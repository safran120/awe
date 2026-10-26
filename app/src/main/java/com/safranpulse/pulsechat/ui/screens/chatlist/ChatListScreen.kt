package com.safranpulse.pulsechat.ui.screens.chatlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.safranpulse.pulsechat.ui.components.*
import com.safranpulse.pulsechat.navigation.Screen
import com.safranpulse.pulsechat.ui.theme.PulseChatTheme

@Composable
fun ChatListScreen(navController: NavHostController) {
    val viewModel: ChatListViewModel = hiltViewModel()

    PulseChatTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(
                            text = "Messages",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    elevation = 0.dp,
                    actions = {
                        IconButton(
                            onClick = { navController.navigate(Screen.GroupChat.route) },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.GroupAdd,
                                contentDescription = "New group chat",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                PingSectionTitle(
                    title = "Recent conversations",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (viewModel.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                viewModel.error?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(
                        items = viewModel.chats,
                        key = { it.id }
                    ) { chat ->
                        PingListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = rememberRipple(bounded = true),
                                    onClick = {
                                        viewModel.markChatAsRead(chat.id)
                                        navController.navigate(Screen.Chat.createRoute(chat.id))
                                    }
                                ),
                            leading = {
                                if (chat.imageUrl.isNotEmpty()) {
                                    PingAvatar(
                                        model = chat.imageUrl,
                                        size = 52.dp,
                                        contentDescription = "Chat avatar"
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier.size(52.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = chat.name.first().uppercase(),
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    }
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                        shape = MaterialTheme.shapes.extraLarge
                                    )
                                }
                            },
                            headline = {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = chat.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                    Text(
                                        text = chat.lastMessage.ifEmpty { "Start conversation" },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1
                                    )
                                }
                            },
                            trailing = {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = formatTime(chat.updatedAt),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    if (chat.unreadCount > 0) {
                                        Box(
                                            modifier = Modifier
                                                .padding(top = 4.dp)
                                                .padding(horizontal = 6.dp, vertical = 4.dp)
                                                .background(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = MaterialTheme.shapes.extraLarge
                                                )
                                        ) {
                                            Text(
                                                text = chat.unreadCount.coerceAtMost(99).toString(),
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontSize = 10.sp,
                                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                                ),
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        }
                                    }
                                }
                            }
                        )
                        PingDivider(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                PingButton(
                    onClick = { navController.navigate(Screen.Contacts.route) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "Start new chat",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private fun formatTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000}m ago"
        diff < 86400000 -> "${diff / 3600000}h ago"
        else -> timestamp.toString()
    }
}
