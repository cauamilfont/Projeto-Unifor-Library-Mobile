package com.example.telaslivros

data class PendentReview(
    val id: Int,
    val userId: Int,
    val bookTitle: String,
    val authorName: String,
    val commentContent: String,
    val ratingContent: Int,
    val ratingPhysical: Int
)