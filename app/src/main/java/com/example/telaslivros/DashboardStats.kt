package com.example.telaslivros

data class DashboardStats(
    val pendingRequests: Int,
    val rentedBooks: Int,
    val totalBooks: Int
)