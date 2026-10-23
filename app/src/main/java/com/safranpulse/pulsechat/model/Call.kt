package com.safranpulse.pulsechat.model

data class Call(
    val id: String = "",
    val callerId: String = "",
    val calleeId: String = "",
    val status: String = "ringing",
    val offer: String = "",
    val answer: String = "",
    val iceCandidates: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) {
    constructor() : this("", "", "", "ringing", "", "", emptyList(), System.currentTimeMillis())
}
