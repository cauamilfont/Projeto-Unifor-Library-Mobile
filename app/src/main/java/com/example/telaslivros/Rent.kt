package com.example.telaslivros

import java.sql.Date
import java.time.LocalDate

data class Rent(
    val id: Int = 0,
    val userId: Int,
    val bookId: Int,
    val userName : String,
    val status: Status = Status.PENDENTE,
    val imageUrl: String = "",
    val requestDate: LocalDate = LocalDate.now(),
    val initialDate: LocalDate,
    val finalDate: LocalDate,
    val withdrawalCode: String = "",
    val book : Book?
)