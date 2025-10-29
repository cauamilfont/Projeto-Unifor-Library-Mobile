package com.example.telaslivros

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate

class HistoryRentsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryRentsAdapter // <-- USA O NOVO ADAPTER

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Reutiliza o mesmo layout de fragmento (que só tem o RecyclerView)
        return inflater.inflate(R.layout.fragment_rents_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pega os dados mockados (somente os finalizados)
        val mockData = criarMockSolicitacoes("Histórico") // Ou "Finalizados"

        // Configura o RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewRents) // O ID é o do fragment_rents_list.xml
        adapter = HistoryRentsAdapter(mockData) // <-- USA O NOVO ADAPTER
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }


    private fun criarMockSolicitacoes(status: String?): List<Rent> {
        val todasSolicitacoes = listOf(
            Rent("A Culpa é das Estrelas", "John Green", "João Luiz","Pendente", "https://m.media-amazon.com/images/I/811ivBP1rsL._UF1000,1000_QL80_.jpg",
                LocalDate.of(2025, 10, 26), LocalDate.of(2025, 10, 28), LocalDate.of(2025, 11, 4)),
            Rent("O Pequeno Príncipe", "Antoine de Saint-Exupéry", "João Luiz", "Pendente", "https://m.media-amazon.com/images/I/81TmOZIXvzL._UF1000,1000_QL80_.jpg",
                LocalDate.of(2025, 10, 27), LocalDate.of(2025, 11, 1),  LocalDate.of(2025, 11, 15)),
            Rent("Duna", "Frank Herbert", "João Luiz","Aprovado", "https://m.media-amazon.com/images/I/81zN7udGRUL.jpg",
                LocalDate.of(2025, 10, 20), LocalDate.of(2025, 10, 22), LocalDate.of(2025, 10, 29)),
            Rent("1984", "George Orwell", "João Luiz","Histórico", "https://m.media-amazon.com/images/I/61t0bwt1s3L._AC_UF1000,1000_QL80_.jpg",
                LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 2), LocalDate.of(2025, 9, 9)),
            Rent("Harry Potter", "J.K. Rowling", "João Luiz","Histórico", "https://m.media-amazon.com/images/I/61jgm6ooXzL._AC_UF1000,1000_QL80_.jpg",
                LocalDate.of(2025, 8, 15), LocalDate.of(2025, 8, 20), LocalDate.of(2025, 9, 3))
        )
        if (status == null) return emptyList()
        return todasSolicitacoes.filter { it.status.equals(status, ignoreCase = true) }
    }

    companion object {
        fun newInstance(): HistoryRentsFragment {
            return HistoryRentsFragment()
        }
    }
}