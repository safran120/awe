package com.safranpulse.pulsechat

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavDeepLinkBuilder
import com.safranpulse.pulsechat.data.UserRepository
import com.safranpulse.pulsechat.navigation.NavigationGraph
import com.safranpulse.pulsechat.navigation.Screen
import com.safranpulse.pulsechat.ui.theme.PulseChatTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userRepository: UserRepository

    private var pendingChatId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
        setContent {
            PulseChatTheme {
                val navController = rememberNavController()
                NavigationGraph(navController)
                LaunchedEffect(pendingChatId) {
                    pendingChatId?.let { chatId ->
                        navController.navigate(Screen.Chat.createRoute(chatId))
                        pendingChatId = null
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val action = intent?.action
        when (action) {
            "OPEN_CHAT", "REPLY_MESSAGE" -> {
                intent?.getStringExtra("chatId")?.let { chatId ->
                    pendingChatId = chatId
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch { userRepository.setPresence("Online") }
    }

    override fun onStop() {
        super.onStop()
        lifecycleScope.launch { userRepository.setPresence("Offline") }
    }
}

