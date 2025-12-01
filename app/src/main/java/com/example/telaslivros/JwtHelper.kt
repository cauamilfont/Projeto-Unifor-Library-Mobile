package com.example.telaslivros

import android.util.Log
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.SecretKey

object JwtHelper {


    private const val SECRET_STRING = "uma_chave_super_secreta_para_o_projeto_faculdade_123456"
    private val SECRET_KEY: SecretKey = Keys.hmacShaKeyFor(SECRET_STRING.toByteArray(
        StandardCharsets.UTF_8))

    private const val EXPIRATION_TIME = 86400000L

    fun generateToken(userId: Int, role: String): String {
        val now = Date()
        val expiryDate = Date(now.time + EXPIRATION_TIME)

        return Jwts.builder()
            .setSubject(userId.toString()) // ID do usuário como "assunto"
            .claim("role", role)           // Papel do usuário (admin/user)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SECRET_KEY)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
            Log.e("TOKEN_RESULT", "TOKEN  Autotizado")
            true
        } catch (e: Exception) {
            Log.e("TOKEN_RESULT_FALSE", "TOKEN Não Autotizado " + e.message)
            false
        }
    }

    fun getUserIdFromToken(token: String): Int? {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .body
            claims.subject.toInt()
        } catch (e: Exception) {
            null
        }
    }
}