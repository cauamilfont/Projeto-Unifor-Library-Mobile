package com.example.telaslivros

data class NotificationConfig(
    val userId: Int,
    // Configurações de Usuário Comum
    var notificaAluguel: Boolean = true,
    var lembreteDevolucao: Boolean = true,
    var lembreteRetirada: Boolean = true,
    var novosLivros: Boolean = true,
    // Configurações de Admin
    var novosPedidosAdmin: Boolean = false,
    var devolucoesAtrasadasAdmin: Boolean = false,
    var edicaoLivroAdmin: Boolean = false,
    var adicaoLivroAdmin: Boolean = false,
    var relatosBugAdmin: Boolean = false
)