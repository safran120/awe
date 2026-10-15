package com.pingmate.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Home : Screen("home")
    object Chat : Screen("chat/{chatId}") {
        companion object {
            fun createRoute(chatId: String) = "chat/$chatId"
        }
    }
    object Profile : Screen("profile")
    object Contacts : Screen("contacts")
}
