package com.safranpulse.pulsechat.ui.screens.chat

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.safranpulse.pulsechat.model.Message
import com.safranpulse.pulsechat.ui.components.*
import com.safranpulse.pulsechat.ui.theme.PulseChatTheme

@Composable
fun ChatScreen(chatId: String) {
    val viewModel: ChatViewModel = hiltViewModel()
    val messages by viewModel.messages.collectAsState()
    val typing by viewModel.typing.collectAsState()
    val isSearching by viewModel.isSearchingField.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    var messageText by rememberSaveable { mutableStateOf("") }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(chatId) { viewModel.start(chatId) }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { viewModel.sendMedia(it.toString(), "image") }
    }
    val audioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.sendMedia(it.toString(), "audio") }
    }

    val typingUsers = typing.filter { (_, ts) -> System.currentTimeMillis() - ts < 5000 }
        .keys.filter { it != viewModel.currentUserId }

    val displayedMessages = if (isSearching) searchResults else messages

    PulseChatTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(
                            text = "Conversation",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    elevation = 0.dp
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                error?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 16.dp, top = 8.dp)
                    )
                }

                if (typingUsers.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = typingUsers.joinToString(", ") + " ${if (typingUsers.size == 1) "is typing" else "are typing"}…",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontStyle = androidx.compose.ui.text.FontStyle.Italic
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                if (!isSearching) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                viewModel.searchMessages(it)
                            },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Search messages") },
                            trailingIcon = {
                                if (searchQuery.isNotBlank()) {
                                    IconButton(
                                        onClick = {
                                            searchQuery = ""
                                            viewModel.searchMessages("")
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Clear search",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp)
                ) {
                    items(
                        items = displayedMessages,
                        key = { it.id }
                    ) { msg ->
                        val isMine = msg.senderId == viewModel.currentUserId

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .padding(
                                    start = if (isMine) 64.dp else 16.dp,
                                    end = if (isMine) 16.dp else 64.dp
                                ),
                            horizontalAlignment = if (isMine) Alignment.End else Alignment.Start
                        ) {
                            if (msg.type == "text") {
                                PingCard(
                                    modifier = Modifier
                                        .clipToBounds()
                                        .clickable(
                                            indication = rememberRipple(bounded = true),
                                            onClick = {
                                                if (isMine && msg.status == "sending") return@clickable
                                                if (isMine) {
                                                    viewModel.editMessage(msg.id, msg.content)
                                                }
                                            }
                                        ),
                                    shape = if (isMine) {
                                        androidx.compose.foundation.shape.RoundedCornerShape(
                                            topLeft = 16.dp,
                                            topRight = 16.dp,
                                            bottomLeft = 16.dp,
                                            bottomRight = 4.dp
                                        )
                                    } else {
                                        androidx.compose.foundation.shape.RoundedCornerShape(
                                            topLeft = 16.dp,
                                            topRight = 16.dp,
                                            bottomLeft = 4.dp,
                                            bottomRight = 16.dp
                                        )
                                    }
                                ) {
                                    Text(
                                        text = if (msg.editedAt != null) msg.content + " (edited)" else msg.content,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isMine) Color.White else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(12.dp, 10.dp)
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = when (msg.status) {
                                            "sending" -> "Sending…"
                                            "sent" -> "Sent"
                                            "delivered" -> "Delivered"
                                            "read" -> "Read"
                                            else -> "Sending…"
                                        },
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontSize = 10.sp
                                        ),
                                        color = if (isMine) {
                                            when (msg.status) {
                                                "read" -> MaterialTheme.colorScheme.primary
                                                "delivered" -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                                "sent" -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                                else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                            }
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                        }
                                    )

                                    listOf("👍", "❤️", "😂").forEach { emoji ->
                                        TextButton(
                                            onClick = { viewModel.react(msg.id, emoji) },
                                            modifier = Modifier.padding(vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "${emoji}${msg.reactions[emoji]?.let { " $it" } ?: ""}",
                                                style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }

                                    if (isMine && msg.readBy.isNotEmpty()) {
                                        Text(
                                            text = "Read ${msg.readBy.size}",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontSize = 10.sp
                                            ),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            } else {
                                PingCard(
                                    modifier = Modifier
                                        .clipToBounds()
                                        .clickable(
                                            indication = rememberRipple(bounded = true),
                                            onClick = {
                                                if (isMine && msg.status == "sending") return@clickable
                                            }
                                        ),
                                    shape = if (isMine) {
                                        androidx.compose.foundation.shape.RoundedCornerShape(
                                            topLeft = 16.dp,
                                            topRight = 16.dp,
                                            bottomLeft = 16.dp,
                                            bottomRight = 4.dp
                                        )
                                    } else {
                                        androidx.compose.foundation.shape.RoundedCornerShape(
                                            topLeft = 16.dp,
                                            topRight = 16.dp,
                                            bottomLeft = 4.dp,
                                            bottomRight = 16.dp
                                        )
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                                                shape = MaterialTheme.shapes.medium
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "[${msg.type.uppercase()}]",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontSize = 10.sp,
                                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                            ),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = when (msg.status) {
                                            "sending" -> "Sending…"
                                            "sent" -> "Sent"
                                            "delivered" -> "Delivered"
                                            "read" -> "Read"
                                            else -> "Sending…"
                                        },
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontSize = 10.sp
                                        ),
                                        color = if (isMine) {
                                            when (msg.status) {
                                                "read" -> MaterialTheme.colorScheme.primary
                                                "delivered" -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                                "sent" -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                                else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                            }
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = {
                            messageText = it
                            viewModel.setTyping(it.isNotBlank())
                        },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Type a message…") },
                        leadingIcon = { Icon(Icons.Default.AddAPhoto, contentDescription = null) },
                        trailingIcon = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                IconButton(
                                    onClick = { audioLauncher.launch("audio/*") },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = "Voice message",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                IconButton(
                                    onClick = { imagePicker.launch("image/*") },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Image,
                                        contentDescription = "Attach image",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    )

                    PingButton(
                        onClick = {
                            viewModel.sendText(messageText)
                            messageText = ""
                        },
                        modifier = Modifier.size(48.dp),
                        enabled = messageText.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send message",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
