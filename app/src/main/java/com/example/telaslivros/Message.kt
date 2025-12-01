package com.example.telaslivros

data class Message(
    val id: Long = System.currentTimeMillis(),
    var text: String,
    val isUser: Boolean,
    val isStreaming: Boolean = false
)