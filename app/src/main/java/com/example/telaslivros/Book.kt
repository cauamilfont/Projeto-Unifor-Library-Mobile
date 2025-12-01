package com.example.telaslivros


import android.service.quicksettings.Tile

data class Book(
    val id : Int,
    val title: String,
    val author : String,
    val imageURL : String,
    val genre : String?,
    val stock : Int,
    val synopsis : String,
    val bookQuality : Float,
    val physicalQuality : Float
)