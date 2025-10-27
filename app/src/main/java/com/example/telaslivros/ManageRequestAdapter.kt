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

class ManageRequestAdapter (private val rents: List<Rent>) :
    RecyclerView.Adapter<ManageRequestAdapter.ManageRequestViewHolder>() {

    class ManageRequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTitulo_Item)
        val user: TextView = view.findViewById(R.id.tvUsuario_Item)
        val detailsBtn: Button = view.findViewById(R.id.btnDetalhes)

    }

    // 2. onCreateViewHolder (Criador de Visualizações)
    // Chamado quando o RecyclerView precisa criar um novo "card"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageRequestViewHolder {
        // Infla (cria) o layout XML do item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_solicitacao, parent, false)
        return ManageRequestViewHolder(view)
    }

    // 3. getItemCount (Contador de Itens)
    // Diz ao RecyclerView quantos itens existem na lista
    override fun getItemCount() = rents.size

    // 4. onBindViewHolder (Vinculador de Dados)
    // Pega os dados de uma posição da lista e os coloca no "card"
    override fun onBindViewHolder(holder: ManageRequestViewHolder, position: Int) {
        val rent = rents[position] // Pega o item da lista

        // Coloca os dados nos TextViews
        holder.title.text = "Título: ${rent.title}"
        holder.user.text = "Autor: ${rent.user}"
        val status = rent.status


        if(status.equals("Pendente", ignoreCase = true)) {
            holder.detailsBtn.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, DetailsRequestActivity::class.java)

                val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                intent.putExtra("TITLE", rent.title)
                intent.putExtra("AUTHOR", rent.author)
                intent.putExtra("USER", rent.user)
                intent.putExtra("R_DATE", rent.requestDate.format(formatter))
                intent.putExtra("I_DATE", rent.initialDate.format(formatter))
                intent.putExtra("F_DATE", rent.finalDate.format(formatter))
                intent.putExtra("IMAGE_URL",rent.imageUrl )

                context.startActivity(intent)
            }
        }




    }
}