package com.pingmate.ui.screens.chat

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pingmate.ui.theme.PingMateTheme
import com.pingmate.ui.theme.PrimaryBlue
import com.pingmate.ui.theme.Neutral100
import com.pingmate.model.Message

@Composable
fun ChatScreen(chatId: String, currentUserId: String) {
    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Message>() }

    // Image picker launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: String? ->
        uri?.let {
            messages.add(Message(chatId = chatId, senderId = currentUserId, content = "Image: $it", type = "image"))
        }
    }

    PingMateTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(modifier = Modifier.weight(1f).padding(8.dp)) {
                    items(messages) { msg ->
                        val isMine = msg.senderId == currentUserId
                        Surface(
                            color = if (isMine) PrimaryBlue else Neutral100,
                            contentColor = if (isMine) Color.White else Color.Black,
                            shape = if (isMine) {
                                androidx.compose.foundation.shape.RoundedCornerShape(12.dp, 12.dp, 0.dp, 12.dp)
                            } else {
                                androidx.compose.foundation.shape.RoundedCornerShape(12.dp, 12.dp, 12.dp, 0.dp)
                            },
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text(
                                text = msg.content,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    TextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { launcher.launch("*/*") }) {
                        Text("+") // Use an icon in real app
                    }
                    Button(onClick = {
                        messages.add(Message(chatId = chatId, senderId = currentUserId, content = messageText))
                        messageText = ""
                    }) {
                        Text("Send")
                    }
                }
            }
        }
    }
}
