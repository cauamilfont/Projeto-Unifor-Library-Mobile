package com.example.telaslivros

import java.time.LocalDate

data class Rent(
    val title: String,
    val author: String,
    val user : String,
    val status: String,
    val imageUrl : String,
    val requestDate: LocalDate,
    val initialDate : LocalDate,
    val finalDate: LocalDate
)