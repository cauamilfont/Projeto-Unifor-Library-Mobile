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


    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    class ManageRequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title: TextView = view.findViewById(R.id.tvTitulo_Item)
        val user: TextView = view.findViewById(R.id.tvUsuario_Item)
        val detailsBtn: Button = view.findViewById(R.id.btnDetalhes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_solicitacao, parent, false)
        return ManageRequestViewHolder(view)
    }

    override fun getItemCount() = rents.size

    override fun onBindViewHolder(holder: ManageRequestViewHolder, position: Int) {
        val rent = rents[position]


        holder.title.text = "Título: ${rent.book?.title}"


        holder.user.text = "Usuário: ${rent.userName}"


        holder.detailsBtn.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailsRequestActivity::class.java)


            intent.putExtra("EXTRA_TRANSACTION_ID", rent.id.toString())
            intent.putExtra("RENT_STATUS", rent.status.name)
            intent.putExtra("BOOK_ID", rent.bookId)

            intent.putExtra("BOOK_TITLE", rent.book?.title)
            intent.putExtra("BOOK_AUTHOR", rent.book?.author)

            intent.putExtra("USER_INFO", rent.userId.toString())


            intent.putExtra("R_DATE", rent.requestDate.format(formatter))
            intent.putExtra("I_DATE", rent.initialDate.format(formatter))
            intent.putExtra("F_DATE", rent.finalDate.format(formatter))

            context.startActivity(intent)
        }


        if (rent.status == Status.PENDENTE || rent.status == Status.APROVADO) {
            holder.detailsBtn.visibility = View.VISIBLE
            holder.detailsBtn.text = if (rent.status == Status.PENDENTE) "Detalhes" else "Devolução"
        } else {
            holder.detailsBtn.visibility = View.INVISIBLE
        }

    }
}