package com.pingmate.navigation

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pingmate.ui.screens.auth.AuthScreen
import com.pingmate.ui.screens.chat.ChatScreen
import com.pingmate.ui.screens.contacts.ContactsScreen
import com.pingmate.ui.screens.home.HomeScreen
import com.pingmate.ui.screens.profile.ProfileScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Auth.route) {
        composable(Screen.Auth.route) {
            AuthScreen()
        }
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Chat.route) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ChatScreen(chatId)
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
        composable(Screen.Contacts.route) {
            ContactsScreen()
        }
    }
}
