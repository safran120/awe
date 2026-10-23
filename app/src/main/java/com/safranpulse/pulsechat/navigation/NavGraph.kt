package com.safranpulse.pulsechat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.safranpulse.pulsechat.ui.screens.auth.AuthScreen
import com.safranpulse.pulsechat.ui.screens.call.CallScreen
import com.safranpulse.pulsechat.ui.screens.chat.ChatScreen
import com.safranpulse.pulsechat.ui.screens.chatlist.ChatListScreen
import com.safranpulse.pulsechat.ui.screens.contacts.ContactsScreen
import com.safranpulse.pulsechat.ui.screens.groupchat.GroupChatScreen
import com.safranpulse.pulsechat.ui.screens.profile.ProfileScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
        Screen.ChatList.route
    } else {
        Screen.Auth.route
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Auth.route) {
            AuthScreen(onAuthSuccess = {
                navController.navigate(Screen.ChatList.route) {
                    popUpTo(Screen.Auth.route) { inclusive = true }
                }
            })
        }
        composable(Screen.ChatList.route) {
            ChatListScreen(navController)
        }
        composable(Screen.Chat.route) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ChatScreen(chatId)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(onLoggedOut = {
                navController.navigate(Screen.Auth.route) {
                    popUpTo(Screen.ChatList.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Contacts.route) {
            ContactsScreen(navController)
        }
        composable(Screen.Call.route) { backStackEntry ->
            val callId = backStackEntry.arguments?.getString("callId") ?: ""
            CallScreen(callId)
        }
        composable(Screen.GroupChat.route) { backStackEntry ->
            val participants = backStackEntry.arguments?.getStringArrayList("participants") ?: emptyList()
            GroupChatScreen(navController, participants)
        }
    }
}
