package com.example.telaslivros

data class Comment (
    val id: String,
    val bookTitle: String,
    val commentContent: String,
    val ratingContent: Float,
    val ratingPhysical: Float,
    val authorName: String
)