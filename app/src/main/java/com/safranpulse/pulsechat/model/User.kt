package com.safranpulse.pulsechat.model

data class User(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val displayName: String = "",
    val bio: String = "",
    val avatarUrl: String = "",
    val status: String = "Offline",
    val lastSeen: Long = System.currentTimeMillis(),
    val contacts: List<String> = emptyList()
) {
    constructor() : this("", "", "", "", "", "", "Offline", System.currentTimeMillis(), emptyList())
}
