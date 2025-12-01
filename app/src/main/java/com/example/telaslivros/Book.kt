package com.example.telaslivros


import android.os.Parcelable
import android.service.quicksettings.Tile
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val id: Int = 0,
    val title: String,
    val author: String,
    // MUDANÇA: De String (URL) para ByteArray (Imagem real)
    val coverImage: ByteArray? = null,
    val synopsis: String?,
    val bookQuality: Float?,
    val physicalQuality: Float?,
    val stock: Int,
    val genre: String
) : Parcelable {
    // Necessário sobrescrever equals/hashCode por causa do Array
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Book
        if (id != other.id) return false
        if (!coverImage.contentEquals(other.coverImage)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (coverImage?.contentHashCode() ?: 0)
        return result
    }
}