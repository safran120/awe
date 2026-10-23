package com.safranpulse.pulsechat.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object ChatList : Screen("chatlist")
    object Chat : Screen("chat/{chatId}") {
        companion object {
            fun createRoute(chatId: String) = "chat/$chatId"
        }
    }
    object Profile : Screen("profile")
    object Contacts : Screen("contacts")
    object Call : Screen("call/{callId}") {
        companion object {
            fun createRoute(callId: String) = "call/$callId"
        }
    }
    object GroupChat : Screen("groupchat")
}

