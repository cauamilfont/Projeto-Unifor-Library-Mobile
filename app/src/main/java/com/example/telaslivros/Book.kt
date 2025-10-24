package com.example.telaslivros

import android.service.quicksettings.Tile

data class Book(
    val title: String,
    val author : String?,
    val imageURL : String,
    val synopsis : String?,
    val bookQuality : Double?,
    val physicalQuality : Double?
)