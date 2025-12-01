package com.example.telaslivros

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.FunctionDeclaration
import com.google.ai.client.generativeai.type.FunctionResponsePart
import com.google.ai.client.generativeai.type.Schema
import com.google.ai.client.generativeai.type.Tool
import com.google.ai.client.generativeai.type.defineFunction
import kotlinx.coroutines.launch
import com.google.ai.client.generativeai.type.FunctionType
import org.json.JSONObject
import com.google.ai.client.generativeai.type.content



class ChatBotActivity : BaseActivity() {

    override fun getBottomNavItemId() = 0;


    private lateinit var rvMessages: RecyclerView
    lateinit var userMessage : EditText
    private val geminiApiKey = "AIzaSyDq3a457wvWxkm5WO_fncNSrMV1fFo2QB8"
    private lateinit var chat : Chat
    private lateinit var chatAdapter: ChatAdapter
    private val messageList = mutableListOf<Message>()
    lateinit var btnSend : ImageButton
    lateinit var btnBack : ImageView
    lateinit var sessionPrefs : SharedPreferences
    lateinit var userName : String

    private var currentUserId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        userMessage = findViewById(R.id.etMensagem)
        btnSend =findViewById(R.id.btnEnviar)
        btnBack = findViewById(R.id.backButton)
        rvMessages = findViewById(R.id.rvMessages)

        chatAdapter = ChatAdapter(this, messageList)

        rvMessages.adapter = chatAdapter

        val layoutManager = LinearLayoutManager(this)
         layoutManager.stackFromEnd = true
        rvMessages.layoutManager = layoutManager

        sessionPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        userName = sessionPrefs.getString("USER_NAME", "") ?: ""
        currentUserId = sessionPrefs.getInt("USER_ID_INT", 0)
        addInitialMessages(userName)


        val myRentsFunction = FunctionDeclaration(
            name = "buscarMeusAlugueis",
            description = "Retorna a lista de livros do usuário.",
            parameters = listOf(
                Schema(
                    name = "parameters",
                    description = "Sem parâmetros",
                    type = FunctionType.OBJECT,
                    properties = emptyMap(),
                    required = emptyList(),
                    format = null,
                    nullable = false,
                    enum = null,
                    items = null
                )
            ),
            requiredParameters = emptyList()
        )

        val booksFunction = FunctionDeclaration(
            name = "buscarLivrosDisponiveis",
            description = "Retorna livros disponíveis.",
            parameters = listOf(
                Schema(
                    name = "parameters",
                    description = "Sem parâmetros",
                    type = FunctionType.OBJECT,
                    properties = emptyMap(),
                    required = emptyList(),
                    format = null,
                    nullable = false,
                    enum = null,
                    items = null
                )
            ),
            requiredParameters = emptyList()
        )

        val topRatedBooksFunction = FunctionDeclaration(
            name = "buscarLivrosMelhorAvaliados",
            description = "Retorna uma lista dos livros com as melhores notas e avaliações dos usuários.",
            parameters = listOf(
                Schema(
                    name = "parameters",
                    description = "Sem parâmetros",
                    type = FunctionType.OBJECT,
                    properties = emptyMap(),
                    required = emptyList(),
                    format = null, nullable = false, enum = null, items = null
                )
            ),
            requiredParameters = emptyList()
        )

        val topRatedTool = Tool(listOf(topRatedBooksFunction))

        val myRentsTool = Tool(
            functionDeclarations = listOf(myRentsFunction)
        )

        val booksTool = Tool(
            functionDeclarations = listOf(booksFunction)
        )

        val generativeModel = GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = geminiApiKey,
            tools = listOf(myRentsTool, booksTool, topRatedTool)
        )

        chat = generativeModel.startChat(history = emptyList())



        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()




        btnSend.setOnClickListener {
            val message = userMessage.text.toString()
            if(message.isNotBlank()){
                Log.e("CJATBOT", "enviando mensgaem")
                sendMessage(message)
                userMessage.text.clear()
            }
            Log.e("CJATBOTtt", "mensagem em branco")
        }
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun addInitialMessages(userName : String) {
        addMessage(Message(text = "Olá $userName! Como posso ajudar?", isUser = false))
    }

    private fun addMessage(message: Message) {
        messageList.add(message)
        chatAdapter.notifyItemInserted(messageList.size - 1)
        rvMessages.scrollToPosition(messageList.size - 1)
    }
    private fun sendMessage(userMessage: String) {
        addMessage(Message(text = userMessage, isUser = true))

        // Placeholder para a resposta
        val botMessagePlaceholder = Message(text = "Pensando...", isUser = false, isStreaming = true)
        messageList.add(botMessagePlaceholder)
        val botPosition = messageList.size - 1
        chatAdapter.notifyItemInserted(botPosition)
        rvMessages.scrollToPosition(botPosition)

        lifecycleScope.launch {
            try {

                var response = chat.sendMessage(prePrompt() + userMessage)

                while (response.functionCalls.isNotEmpty()) {

                    val functionCall = response.functionCalls.first()
                    val functionName = functionCall.name


                    val toolResult = when (functionName) {
                        "buscarMeusAlugueis" -> DatabaseHelper.getMyRentsForChat(currentUserId)
                        "buscarLivrosDisponiveis" -> DatabaseHelper.getAvailableBooksForChat()
                        "buscarLivrosMelhorAvaliados" -> DatabaseHelper.getTopRatedBooksForChat()
                        else -> "Função desconhecida."
                    }


                    val jsonResponse = JSONObject()
                    jsonResponse.put("result", toolResult)

                    // Cria a resposta da função usando o JSONObject
                    val functionResponsePart = FunctionResponsePart(
                        functionName,
                        jsonResponse
                    )


                    response = chat.sendMessage(
                        content {
                            part(functionResponsePart)
                        }
                    )
                }


                val finalText = response.text ?: "Sem resposta."


                messageList[botPosition] = botMessagePlaceholder.copy(
                    text = finalText,
                    isStreaming = false
                )
                chatAdapter.notifyItemChanged(botPosition)

            } catch (e: Exception) {
                Log.e("GEMINI_CHAT", "Erro: ${e.message}")
                messageList[botPosition] = botMessagePlaceholder.copy(
                    text = "Desculpe, tive um problema ao processar seu pedido.",
                    isStreaming = false
                )
                chatAdapter.notifyItemChanged(botPosition)
            }
        }
    }

    private fun prePrompt(): String {
        return """
            Você é um assistente de um aplicativo de biblioteca, onde é possivel fazer reservas online, logo só responda sobre assuntos relacionados. O nome do usuário é $userName.
            VOCÊ TEM ACESSO A FERRAMENTAS REAIS para buscar dados.
            Se o usuário perguntar sobre 'aluguéis', 'reservas' ou 'status', USE A FERRAMENTA 'buscarMeusAlugueis'.
            Se perguntar sobre livros disponíveis, USE A FERRAMENTA 'buscarLivrosDisponiveis',responda de maneira organizada, clara e formatada.
            Se perguntar sobre 'livros bem avaliados', 'melhores livros' ou 'ranking', USE A FERRAMENTA 'buscarLivrosMelhorAvaliados',responda de maneira organizada, clara e formatada.
            Não invente dados. Use as funções.
            Responda em português.
            Quando for usar as ferramentas para buscar os dados, formate os dados, para informar o usuario de forma limpa e organizada
            ---
            
        """.trimIndent()
    }
    }
