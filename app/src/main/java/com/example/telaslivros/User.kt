package com.example.telaslivros

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class User(
    val id: Int = 0,
    val nomeCompleto: String,
    val email: String,
    val senhaHash: String,
    val telefone: String? = null,
    val cpf: String? = null,
    val cep: String? = null,
    val fotoPerfil: ByteArray? = null,
    val tipoUsuario: UserType = UserType.USER,
    val ativo: Boolean = true,
    val criadoEm: LocalDateTime? = null,
    val atualizadoEm: LocalDateTime? = null
): Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (!fotoPerfil.contentEquals(other.fotoPerfil)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (fotoPerfil?.contentHashCode() ?: 0)
        return result
    }
}
