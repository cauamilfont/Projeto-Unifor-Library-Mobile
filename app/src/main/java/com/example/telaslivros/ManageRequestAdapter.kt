package com.example.telaslivros

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.graphics.Interval
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.time.format.DateTimeFormatter

class ManageRequestAdapter(private val rents: List<Rent>) :
    RecyclerView.Adapter<ManageRequestAdapter.ManageRequestViewHolder>() {

    // Formatador de data para enviar Strings bonitas para a próxima tela
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    class ManageRequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Certifique-se que os IDs no seu layout 'item_solicitacao.xml' são estes
        val title: TextView = view.findViewById(R.id.tvTitulo_Item)
        val user: TextView = view.findViewById(R.id.tvUsuario_Item)
        val detailsBtn: Button = view.findViewById(R.id.btnDetalhes) // O botão "Detalhes"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_solicitacao, parent, false)
        return ManageRequestViewHolder(view)
    }

    override fun getItemCount() = rents.size

    override fun onBindViewHolder(holder: ManageRequestViewHolder, position: Int) {
        val rent = rents[position]

        // 1. Preencher dados do Card
        // Acessamos o título dentro do objeto 'book'
        holder.title.text = "Título: ${rent.book?.title}"

        // Se você tiver o nome do usuário no objeto Rent, use-o.
        // Se não, mostramos o ID por enquanto.
        holder.user.text = "Usuário: ${rent.userName}"

        // 2. Configurar o botão "Detalhes"
        holder.detailsBtn.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailsRequestActivity::class.java)

            // --- PASSANDO DADOS PARA A TELA DE DETALHES ---

            // ID da transação (Essencial para aprovar/recusar no banco)
            intent.putExtra("EXTRA_TRANSACTION_ID", rent.id.toString())

            // Dados do Livro
            intent.putExtra("BOOK_TITLE", rent.book?.title)
            intent.putExtra("BOOK_AUTHOR", rent.book?.author)
            intent.putExtra("BOOK_IMAGE", rent.book?.imageURL) // Para o Glide na outra tela

            // Dados do Usuário
            intent.putExtra("USER_INFO", rent.userId.toString())

            // Datas (Formatadas como String)
            // Usamos o operador ?. para evitar crash se a data for nula (embora não deva ser)
            intent.putExtra("R_DATE", rent.requestDate.format(formatter))
            intent.putExtra("I_DATE", rent.initialDate.format(formatter))
            intent.putExtra("F_DATE", rent.finalDate.format(formatter))

            context.startActivity(intent)
        }

        // Opcional: Se quiser esconder o botão em abas que não sejam "Pendente"

        if (rent.status == Status.PENDENTE) {
            holder.detailsBtn.visibility = View.VISIBLE
        } else {
            holder.detailsBtn.visibility = View.INVISIBLE
        }

    }
}