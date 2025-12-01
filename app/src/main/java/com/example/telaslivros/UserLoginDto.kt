package com.example.telaslivros

data class UserLoginDto (
    val id : Int,
    val name : String,
    val email: String,
    val role: UserType
)