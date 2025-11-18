package com.example.telaslivros

import android.os.StrictMode
import java.sql.Connection
import android.util.Log
import java.security.MessageDigest
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.math.log

object DatabaseHelper {

    private const val HOST = "10.0.2.2"
    private const val PORT = "5432"
    private const val DATABASE = "gestao_livros"


    private const val URL = "jdbc:postgresql://$HOST:$PORT/$DATABASE"
    private const val USER = "bibli"
    private const val PASSWORD = "bibli2025@"

    init{
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    private fun getConnection() : Connection? {
        return try{
            Class.forName("org.postgresql.Driver")
            DriverManager.getConnection(URL, USER, PASSWORD)
        }catch (e: ClassNotFoundException){
            Log.e("DB_ERROR", "Driver PostgreSQL não encontrado. Adicione a dependência no build.gradle", e)
            null
        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro de conexão SQL: ${e.message}", e)
            null
        } catch (e: Exception) {
            Log.e("DB_ERROR", "Erro genérico: ${e.message}", e)
            null
        }
    }

    fun loginUser(email: String, password: String): UserLoginDto?{
        var connection: Connection? = null
        try{
            connection = getConnection()
            if(connection == null)
                return null

            val hashedPassword = hashString(password)
            val sql = "SELECT id, tipo_usuario FROM users WHERE email = ? AND senha_hash = ?"

            val statement = connection.prepareStatement(sql)
            statement.setString(1, email)
            statement.setString(2, hashedPassword)

            val resultSet = statement.executeQuery()
            if (resultSet.next()) {

                val tipoString = resultSet.getString("tipo_usuario")
                val tipoEnum = try {
                    UserType.valueOf(tipoString)
                } catch (e: Exception) {
                    UserType.USER
                }

                return UserLoginDto(
                    id = resultSet.getInt("id"),
                    role = tipoEnum
                )
            }
        }catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao logar: ${e.message}")
            return null
        }finally {
            connection?.close()
        }
        return null
    }


    fun registerUser(nome: String, email: String, password: String): Boolean{
        var connection: Connection? = null
        try {
            connection = getConnection()
            if(connection == null)
                return false

            val hashedPassword = hashString(password)
            val sql = "INSERT INTO users (nome_completo, email, senha_hash, tipo_admin) VALUES (?, ?, ?, 'USER')"

            val statement = connection.prepareStatement(sql)
            statement.setString(1, nome)
            statement.setString(2, email)
            statement.setString(3, hashedPassword)

            val rowsInserted = statement.executeUpdate()
            return rowsInserted > 0
        }catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao cadastrar: ${e.message}")
            return false
        } finally {
            connection?.close()
        }
    }

    fun verifyEmail(email: String): Boolean{
        var connection: Connection? = null
        try {
            connection = getConnection()
            if(connection == null)
                return false

            val sql = "SELECT id FROM users WHERE email = ?"
            val statement = connection.prepareStatement(sql)
            statement.setString(1, email)

            val resultSet = statement.executeQuery()
            return resultSet.next()

    } catch (e: SQLException) {
        Log.e("DB_ERROR", "Erro ao verificar email: ${e.message}")
        return false
    } finally {
        connection?.close()
       }
    }

    fun changePassword(password:String, email: String) : Boolean {
        var connection: Connection? = null
        try {
            connection = getConnection()
            if (connection == null)
                return false

            val hashedPassword = hashString(password)
            val sql = "UPDATE users SET senha_hash = ? WHERE email = ?"
            val statement = connection.prepareStatement(sql)
            statement.setString(1, hashedPassword)
            statement.setString(2, email)

            val rowsAffected = statement.executeUpdate()

            return rowsAffected > 0
        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao mudar senha: ${e.message}")
            return false
        } finally {
            connection?.close()
        }

        }

    fun changeProfile(user : User) : Boolean {
        var connection: Connection? = null
        try {
            connection = getConnection()
            if (connection == null)
                return false



            val sql = "UPDATE users SET nome_completo = ?, telefone = ?, cep = ?, foto_perfil = ? WHERE id = ?"
            val statement = connection.prepareStatement(sql)
            statement.setString(1, user.nomeCompleto)
            statement.setString(2, user.telefone)
            statement.setString(3, user.cep)
            statement.setBytes(4, user.fotoPerfil)
            statement.setInt(5, user.id)

            val rowsAffected = statement.executeUpdate()

            return rowsAffected > 0
        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao mudar senha: ${e.message}")
            return false
        } finally {
            connection?.close()
        }

    }

    fun getUser(id:Int) : User? {

        var connection: Connection? = null
        try {
            connection = getConnection()
            if (connection == null)
                return null

            val sql = "SELECT * FROM users WHERE id = ?"
            val statement = connection.prepareStatement(sql)
            statement.setInt(1, id)

            val resultSet = statement.executeQuery()
            if(resultSet.next()) {
                return mapResultSetToUser(resultSet)
            }


        }catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao carregar usuário: ${e.message}")
            return null
        } finally {
            connection?.close()
        }
        return null
    }



    fun setAdmin() : Boolean{

        var connection: Connection? = null
        try {
            connection = getConnection()
            if(connection == null)
                return false

            Log.e("ADMIN_REGISTER", "Cadastrando admin")
            val hashedPassword = hashString("admin123")
            val sql = "INSERT INTO users (nome_completo, email, senha_hash, tipo_usuario) VALUES (?, ?, ?, 'ADMIN')"

            val statement = connection.prepareStatement(sql)
            statement.setString(1, "admin")
            statement.setString(2, "admin@teste.com")
            statement.setString(3, hashedPassword)

            val rowsInserted = statement.executeUpdate()
            return rowsInserted > 0
        }catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao cadastrar: ${e.message}")
            return false
        } finally {
            connection?.close()
        }
    }




    private fun hashString(input: String): String {
        val bytes = input.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    private fun mapResultSetToUser(resultSet: java.sql.ResultSet): User {
        return User(
            id = resultSet.getInt("id"),
            nomeCompleto = resultSet.getString("nome_completo"),
            email = resultSet.getString("email"),
            senhaHash = resultSet.getString("senha_hash"),
            telefone = resultSet.getString("telefone"),
            cpf = resultSet.getString("cpf"),
            cep = resultSet.getString("cep"),


            fotoPerfil = resultSet.getBytes("foto_perfil"),


            tipoUsuario = try {
                UserType.valueOf(resultSet.getString("tipo_usuario"))
            } catch (e: Exception) {
                UserType.USER
            },

            ativo = resultSet.getBoolean("ativo"),

            criadoEm = resultSet.getTimestamp("criado_em")?.toInstant()
                ?.atZone(java.time.ZoneId.systemDefault())
                ?.toLocalDateTime(),

            atualizadoEm = resultSet.getTimestamp("atualizado_em")?.toInstant()
                ?.atZone(java.time.ZoneId.systemDefault())
                ?.toLocalDateTime()
        )
    }

}
