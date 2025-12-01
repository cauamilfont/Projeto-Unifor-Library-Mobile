package com.example.telaslivros

import android.os.StrictMode
import java.sql.Connection
import android.util.Log
import java.security.MessageDigest
import java.sql.Date
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DatabaseHelper {

    private const val HOST = "10.0.2.2"
    private const val PORT = "5432"
    private const val DATABASE = "gestao_livros"


    private const val URL = "jdbc:postgresql://$HOST:$PORT/$DATABASE"
    private const val USER = "bibli"
    private const val PASSWORD = "bibli2025@"

    init {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    private fun getConnection(): Connection? {
        return try {
            Class.forName("org.postgresql.Driver")
            DriverManager.getConnection(URL, USER, PASSWORD)
        } catch (e: ClassNotFoundException) {
            Log.e(
                "DB_ERROR",
                "Driver PostgreSQL não encontrado. Adicione a dependência no build.gradle",
                e
            )
            null
        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro de conexão SQL: ${e.message}", e)
            null
        } catch (e: Exception) {
            Log.e("DB_ERROR", "Erro genérico: ${e.message}", e)
            null
        }
    }

    fun loginUser(email: String, password: String): UserLoginDto? {
        var connection: Connection? = null
        try {
            connection = getConnection()
            if (connection == null)
                return null

            val hashedPassword = hashString(password)
            val sql =
                "SELECT id, nome_completo, email, tipo_usuario FROM users WHERE email = ? AND senha_hash = ?"

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

                val fullname = resultSet.getString("nome_completo")
                return UserLoginDto(
                    id = resultSet.getInt("id"),
                    name = fullname.substringBefore(" "),
                    email = resultSet.getString("email"),
                    role = tipoEnum
                )
            }
        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao logar: ${e.message}")
            return null
        } finally {
            connection?.close()
        }
        return null
    }


    fun registerUser(nome: String, email: String, password: String, cpf: String): Boolean {
        var connection: Connection? = null
        try {
            connection = getConnection()
            if (connection == null)
                return false

            val hashedPassword = hashString(password)
            val sql =
                "INSERT INTO users (nome_completo, email, cpf, senha_hash, tipo_usuario) VALUES (?, ?, ?, ?, 'USER')"

            val statement = connection.prepareStatement(sql)
            statement.setString(1, nome)
            statement.setString(2, email)
            statement.setString(3, cpf)
            statement.setString(4, hashedPassword)

            val rowsInserted = statement.executeUpdate()
            return rowsInserted > 0
        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao cadastrar: ${e.message}")
            return false
        } finally {
            connection?.close()
        }
    }

    fun verifyEmail(email: String): Int {
        var connection: Connection? = null
        try {
            connection = getConnection()

            if (connection == null) return 0

            val sql = "SELECT id FROM users WHERE email = ?"
            val statement = connection.prepareStatement(sql)
            statement.setString(1, email)

            val resultSet = statement.executeQuery()


            if (resultSet.next()) {

                return resultSet.getInt("id")
            }

            return 0

        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao buscar ID por email: ${e.message}")
            return 0
        } finally {
            connection?.close()
        }
    }

    fun changePassword(password: String, userId: Int): Boolean {
        var connection: Connection? = null
        try {
            connection = getConnection()
            if (connection == null)
                return false

            val hashedPassword = hashString(password)
            val sql = "UPDATE users SET senha_hash = ? WHERE id = ?"
            val statement = connection.prepareStatement(sql)
            statement.setString(1, hashedPassword)
            statement.setInt(2, userId)

            val rowsAffected = statement.executeUpdate()

            return rowsAffected > 0
        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao mudar senha: ${e.message}")
            return false
        } finally {
            connection?.close()
        }

    }

    fun changeProfile(user: User): Boolean {
        var connection: Connection? = null
        try {
            connection = getConnection()
            if (connection == null)
                return false


            val sql =
                "UPDATE users SET nome_completo = ?, telefone = ?, cep = ?, foto_perfil = ? WHERE email = ?"
            val statement = connection.prepareStatement(sql)
            statement.setString(1, user.nomeCompleto)
            statement.setString(2, user.telefone)
            statement.setString(3, user.cep)
            statement.setBytes(4, user.fotoPerfil)
            statement.setString(5, user.email)

            val rowsAffected = statement.executeUpdate()

            return rowsAffected > 0
        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao mudar senha: ${e.message}")
            return false
        } finally {
            connection?.close()
        }

    }

    fun getUser(id: Int): User? {

        var connection: Connection? = null
        try {
            connection = getConnection()
            if (connection == null)
                return null

            val sql = "SELECT * FROM users WHERE id = ?"
            val statement = connection.prepareStatement(sql)
            statement.setInt(1, id)

            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                return mapResultSetToUser(resultSet)
            }


        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao carregar usuário: ${e.message}")
            return null
        } finally {
            connection?.close()
        }
        return null
    }

    fun verifyPassword(password: String, userId: Int): Boolean {
        var connection: Connection? = null
        try {
            connection = getConnection()
            if (connection == null)
                return false

            val hashedPassword = hashString(password)

            val sql = "SELECT senha_hash FROM users where id = ?"
            val statement = connection.prepareStatement(sql)
            statement.setInt(1, userId)
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                val currentPassword = resultSet.getString("senha_hash")
                if (hashedPassword == currentPassword)
                    return true

            }

        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Senha incorreta!: ${e.message}")
            return false
        } finally {
            connection?.close()
        }
        return false
    }


    fun setAdmin(): Boolean {

        var connection: Connection? = null
        try {
            connection = getConnection()
            if (connection == null)
                return false

            Log.e("ADMIN_REGISTER", "Cadastrando admin")
            val hashedPassword = hashString("admin123")
            val sql =
                "INSERT INTO users (nome_completo, email, senha_hash, tipo_usuario) VALUES (?, ?, ?, 'ADMIN')"

            val statement = connection.prepareStatement(sql)
            statement.setString(1, "admin")
            statement.setString(2, "admin@teste.com")
            statement.setString(3, hashedPassword)

            val rowsInserted = statement.executeUpdate()
            return rowsInserted > 0
        } catch (e: SQLException) {
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

    fun getAllRents(userId: Int): List<Rent> {
        val rentList = mutableListOf<Rent>()
        var connection: Connection? = null

        try {
            connection = getConnection()
            if (connection == null) return emptyList()

            // SQL com JOIN para preencher o objeto Book dentro do Rent
            var sql = """
                SELECT 
                    r.id AS rent_id, 
                    r.user_id, 
                    r.livro_id, 
                    r.status, 
                    r.data_inicio, 
                    r.data_solicitacao,
                    r.data_fim, 
                    r.codigo_retirada,
                    b.id AS book_real_id, 
                    b.titulo, 
                    b.autor, 
                    b.sinopse,
                    b.quantidade_estoque,
                    b.genero,
                    u.nome_completo
                FROM solicitacoes_aluguel r
                INNER JOIN livros b ON r.livro_id = b.id
                INNER JOIN USERS u ON r.user_id = u.id
                WHERE r.user_id = ?
                ORDER BY r.id DESC
            """
            if(userId == 0){
                sql = """
                SELECT 
                    r.id AS rent_id, 
                    r.user_id, 
                    r.livro_id, 
                    r.status, 
                    r.data_inicio, 
                    r.data_solicitacao,
                    r.data_fim, 
                    r.codigo_retirada,
                    b.id AS book_real_id, 
                    b.titulo, 
                    b.autor, 
                    b.sinopse,
                    b.quantidade_estoque,
                    b.genero,
                    u.nome_completo
                FROM solicitacoes_aluguel r
                INNER JOIN livros b ON r.livro_id = b.id
                INNER JOIN USERS u ON r.user_id = u.id
                ORDER BY r.id DESC """
            }

            val statement = connection.prepareStatement(sql)
            if(userId != 0)
                statement.setInt(1, userId)
            val resultSet = statement.executeQuery()


            while (resultSet.next()) {


                val bookObj = Book(
                    id = resultSet.getInt("book_real_id"),
                    title = resultSet.getString("titulo"),
                    author = resultSet.getString("autor"),
                    imageURL = "",

                    synopsis = resultSet.getString("sinopse") ?: "",
                    bookQuality = 0.0F,
                    physicalQuality = 0.0F,
                    stock = resultSet.getInt("quantidade_estoque"),
                    genre = resultSet.getString("genero")
                )


                val rent = Rent(
                    id = resultSet.getInt("rent_id"),
                    userId = resultSet.getInt("user_id"),
                    bookId = resultSet.getInt("livro_id"),
                    status = Status.valueOf(resultSet.getString("status")),
                    imageUrl = bookObj.imageURL,


                    requestDate = java.time.LocalDate.parse(
                        resultSet.getDate("data_solicitacao").toString()
                    ),
                    initialDate = java.time.LocalDate.parse(
                        resultSet.getDate("data_inicio").toString()
                    ),
                    finalDate = java.time.LocalDate.parse(resultSet.getDate("data_fim").toString()),

                    withdrawalCode = resultSet.getString("codigo_retirada") ?: "",

                    book = bookObj,
                    userName = resultSet.getString("nome_completo")
                )

                rentList.add(rent)
            }

        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao buscar aluguéis: ${e.message}")
        } finally {
            connection?.close()
        }

        return rentList
    }

    fun checkDisponibility(bookId: Int, initialDate: LocalDate, finalDate: LocalDate): Boolean {
        var connection: Connection? = null
        try {
            connection = getConnection()
            if (connection == null) return false


            val totalStock = getBookStock(bookId, connection)


            if (totalStock <= 0) return false


            val sql = """
                SELECT COUNT(*) 
                FROM solicitacoes_aluguel 
                WHERE livro_id = ? 
                AND status NOT IN ('RECUSADO', 'DEVOLVIDO') 
                AND data_fim > ? 
                AND data_inicio < ?
            """

            val statement = connection.prepareStatement(sql)


            val sqlInitialDate = Date.valueOf(initialDate.toString())
            val sqlFinalDate = Date.valueOf(finalDate.toString())

            statement.setInt(1, bookId)
            statement.setDate(2, sqlInitialDate)
            statement.setDate(3, sqlFinalDate)

            val resultSet = statement.executeQuery()

            var currentlyRentedCount = 0
            if (resultSet.next()) {
                currentlyRentedCount = resultSet.getInt(1)
            }

            Log.d(
                "DB_CHECK",
                "Livro $bookId: Estoque Total=$totalStock, Alugados no período=$currentlyRentedCount"
            )


            return currentlyRentedCount < totalStock

        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao verificar disponibilidade: ${e.message}")
            return false
        } finally {
            connection?.close()
        }
    }

    private fun getBookStock(bookId: Int, conn: Connection): Int {

        val sql = "SELECT quantidade_estoque FROM livros WHERE id = ?"
        val stmt = conn.prepareStatement(sql)
        stmt.setInt(1, bookId)
        val rs = stmt.executeQuery()

        return if (rs.next()) rs.getInt(1) else 0
    }

    fun createRent(rent: Rent): Int {
        var connection: Connection? = null
        try {
            connection = getConnection()
            if (connection == null) return 0

            val sql = """
                INSERT INTO solicitacoes_aluguel 
                (user_id, livro_id, data_inicio, data_fim, codigo_retirada, data_solicitacao) 
                VALUES (?, ?, ?, ?, ?, NOW())
            """


            val statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            val sqlInitialDate = Date.valueOf(rent.initialDate.toString())
            val sqlFinalDate = Date.valueOf(rent.finalDate.toString())


            val codigoRetirada = null

            statement.setInt(1, rent.userId)
            statement.setInt(2, rent.bookId)
            statement.setDate(3, sqlInitialDate)
            statement.setDate(4, sqlFinalDate)
            statement.setString(5, codigoRetirada)

            val rowsInserted = statement.executeUpdate()
            var generatedId = 0

            if (rowsInserted > 0) {
                val generatedKeys = statement.generatedKeys
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1)
                }
            }

            return generatedId

        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao criar aluguel: ${e.message}")
            return 0
        } finally {
            connection?.close()
        }

    }

    fun approveRent(rentId: Int): String? {
        var connection: Connection? = null
        try {
            connection = getConnection()
            if (connection == null) return null


            val novoCodigo = "LIB-${rentId}-${(100..999).random()}"


            val aproveDate = LocalDate.now()
            val sql = """
                UPDATE solicitacoes_aluguel 
                SET status = 'APROVADO', codigo_retirada = ?, data_aprovacao = ?
                WHERE id = ?
            """

            val statement = connection.prepareStatement(sql)
            statement.setString(1, novoCodigo)
            statement.setDate(2, Date.valueOf(aproveDate.toString()))
            statement.setInt(3, rentId)

            val rowsUpdated = statement.executeUpdate()


            return if (rowsUpdated > 0) novoCodigo else null

        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao aprovar: ${e.message}")
            return null
        } finally {
            connection?.close()
        }
    }

    fun declineRent(rentId: Int): Boolean {
        var connection: Connection? = null
        try {
            connection = getConnection()
            if (connection == null) return false


            val sql = """
                UPDATE solicitacoes_aluguel 
                SET status = 'RECUSADO' 
                WHERE id = ?
            """

            val statement = connection.prepareStatement(sql)
            statement.setInt(1, rentId)

            val rowsUpdated = statement.executeUpdate()

            return rowsUpdated > 0

        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao recusar: ${e.message}")
            return false
        } finally {
            connection?.close()
        }
    }


    //FUNÇÔES PARA O CHATBOT

    suspend fun getMyRentsForChat(userId: Int): String {
        val rents = getAllRents(userId)

        if (rents.isEmpty()) return "Você não tem nenhum aluguel ou solicitação no momento."


        val sb = StringBuilder("Aqui estão seus aluguéis:\n")
        for (rent in rents) {
            sb.append("📚 Livro: ${rent.book?.title}\n")
            sb.append("📌 Status: ${rent.status}\n")
            sb.append("📅 Data da retirada: ${formatDate(rent.initialDate)}\n")


            if (rent.status == Status.APROVADO) {
                sb.append("🔐 Código de retirada: ${rent.withdrawalCode}\n")
                sb.append("📆 Devolução até: ${formatDate(rent.finalDate)}\n")
            } else {
                sb.append("📆 Devolução até: ${formatDate(rent.finalDate)}\n")
            }

            sb.append("\n") // separador entre os itens
        }
        return sb.toString()
    }

    suspend fun getAvailableBooksForChat(): String {
        var connection: Connection? = null
        return try {
            connection = getConnection()

            val sql = """
            SELECT titulo, autor, quantidade_estoque 
            FROM livros 
            WHERE quantidade_estoque > 0 
            LIMIT 5
        """.trimIndent()

            val stmt = connection?.prepareStatement(sql)
            val rs = stmt?.executeQuery()

            if (rs == null || !rs.next()) {
                return "Não há livros disponíveis no momento."
            }

            val sb = StringBuilder("📚 **Livros disponíveis no momento:**\n\n")

            do {
                val titulo = rs.getString("titulo")
                val autor = rs.getString("autor")
                val estoque = rs.getInt("quantidade_estoque")

                sb.append(
                    """
                • **$titulo**
                   └ Autor: $autor
                   └ Em estoque: $estoque unidades
                
                """.trimIndent()
                ).append("\n")

            } while (rs.next())

            sb.toString()

        } catch (e: Exception) {
            "Erro ao buscar livros."
        } finally {
            connection?.close()
        }
    }

    fun getWithdrawalCode(rentId: Int): String? {
        var connection: Connection? = null
        try {
            connection = getConnection()
            if (connection == null) return null

            val sql = "SELECT codigo_retirada FROM solicitacoes_aluguel WHERE id = ?"
            val statement = connection.prepareStatement(sql)
            statement.setInt(1, rentId)

            val resultSet = statement.executeQuery()

            if (resultSet.next()) {
                return resultSet.getString("codigo_retirada")
            }
        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao buscar código: ${e.message}")
        } finally {
            connection?.close()
        }
        return null
    }

    fun getNotificationConfig(userId: Int): NotificationConfig {
        var connection: Connection? = null
        try {
            connection = getConnection()
            // Se não conectar, retorna padrão (tudo true para user, false para admin)
            if (connection == null) return NotificationConfig(userId)

            val sql = "SELECT * FROM notificacoes_config WHERE user_id = ?"
            val statement = connection.prepareStatement(sql)
            statement.setInt(1, userId)

            val resultSet = statement.executeQuery()

            if (resultSet.next()) {
                return NotificationConfig(
                    userId = userId,
                    notificaAluguel = resultSet.getBoolean("notifica_aluguel"),
                    lembreteDevolucao = resultSet.getBoolean("lembrete_devolucao"),
                    lembreteRetirada = resultSet.getBoolean("lembrete_retirada"),
                    novosLivros = resultSet.getBoolean("novos_livros"),

                    novosPedidosAdmin = resultSet.getBoolean("novos_pedidos_admin"),
                    devolucoesAtrasadasAdmin = resultSet.getBoolean("devolucoes_atrasadas_admin"),
                    edicaoLivroAdmin = resultSet.getBoolean("edicao_livro_admin"),
                    adicaoLivroAdmin = resultSet.getBoolean("adicao_livro_admin"),
                    relatosBugAdmin = resultSet.getBoolean("relatos_bug_admin")
                )
            }

            return NotificationConfig(userId)

        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao buscar notificações: ${e.message}")
            return NotificationConfig(userId)
        } finally {
            connection?.close()
        }
    }


    fun saveNotificationConfig(config: NotificationConfig): Boolean {
        var connection: Connection? = null
        try {
            connection = getConnection()
            if (connection == null) return false

            val sql = """
                INSERT INTO notificacoes_config (
                    user_id, 
                    notifica_aluguel, lembrete_devolucao, lembrete_retirada, novos_livros,
                    novos_pedidos_admin, devolucoes_atrasadas_admin, edicao_livro_admin, adicao_livro_admin, relatos_bug_admin
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT (user_id) 
                DO UPDATE SET 
                    notifica_aluguel = EXCLUDED.notifica_aluguel,
                    lembrete_devolucao = EXCLUDED.lembrete_devolucao,
                    lembrete_retirada = EXCLUDED.lembrete_retirada,
                    novos_livros = EXCLUDED.novos_livros,
                    novos_pedidos_admin = EXCLUDED.novos_pedidos_admin,
                    devolucoes_atrasadas_admin = EXCLUDED.devolucoes_atrasadas_admin,
                    edicao_livro_admin = EXCLUDED.edicao_livro_admin,
                    adicao_livro_admin = EXCLUDED.adicao_livro_admin,
                    relatos_bug_admin = EXCLUDED.relatos_bug_admin
            """

            val statement = connection.prepareStatement(sql)
            // 1. ID
            statement.setInt(1, config.userId)
            // 2-5. Configs User
            statement.setBoolean(2, config.notificaAluguel)
            statement.setBoolean(3, config.lembreteDevolucao)
            statement.setBoolean(4, config.lembreteRetirada)
            statement.setBoolean(5, config.novosLivros)
            // 6-10. Configs Admin
            statement.setBoolean(6, config.novosPedidosAdmin)
            statement.setBoolean(7, config.devolucoesAtrasadasAdmin)
            statement.setBoolean(8, config.edicaoLivroAdmin)
            statement.setBoolean(9, config.adicaoLivroAdmin)
            statement.setBoolean(10, config.relatosBugAdmin)

            val rows = statement.executeUpdate()
            return rows > 0

        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Erro ao salvar notificações: ${e.message}")
            return false
        } finally {
            connection?.close()
        }
    }

    fun formatDate(date: LocalDate?): String {
        val localeBR = Locale.forLanguageTag("pt-BR")
        val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", localeBR)
        return date?.format(formatter) ?: "Data indisponível"
    }
}
