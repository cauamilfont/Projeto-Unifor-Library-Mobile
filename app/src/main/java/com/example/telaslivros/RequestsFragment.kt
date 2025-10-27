package com.example.telaslivros

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate

class RequestsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ManageRequestAdapter
    private var statusFilter: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            statusFilter = it.getString(ARG_STATUS_FILTER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_solicitacoes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvFragmentSolicitacoes)
        val mockData = criarMockSolicitacoes(statusFilter)
        adapter = ManageRequestAdapter(mockData)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    // Função de Mock atualizada para filtrar
    private fun criarMockSolicitacoes(status: String?): List<Rent> {
        val todasSolicitacoes = listOf(
            Rent("A Culpa é das Estrelas", "John Green", "João Luiz","Pendente", "https://m.media-amazon.com/images/I/811ivBP1rsL._UF1000,1000_QL80_.jpg",LocalDate.of(2025, 10, 26),
                LocalDate.of(2025, 10, 28), LocalDate.of(2025, 11, 4) ),
            Rent("O Pequeno Príncipe", "Antoine de Saint-Exupéry", "João Luiz", "Pendente", "https://m.media-amazon.com/images/I/81TmOZIXvzL._UF1000,1000_QL80_.jpg", LocalDate.of(2025, 10, 22),
            LocalDate.of(2025, 10, 29), LocalDate.of(2025, 11, 8) ),
            Rent("Duna", "Frank Herbert", "João Luiz","Aprovado", "https://m.media-amazon.com/images/I/81zN7udGRUL.jpg", LocalDate.of(2025, 10, 26),
                LocalDate.of(2025, 10, 28), LocalDate.of(2025, 11, 4) ),
            Rent("O Senhor dos Anéis", "J.R.R. Tolkien", "João Luiz","Aprovado", "https://m.media-amazon.com/images/I/71ZLavBjpRL._AC_UF1000,1000_QL80_.jpg", LocalDate.of(2025, 10, 26),
                LocalDate.of(2025, 10, 28), LocalDate.of(2025, 11, 4) ),
            Rent("1984", "George Orwell", "João Luiz","Recusado", "https://m.media-amazon.com/images/I/61t0bwt1s3L._AC_UF1000,1000_QL80_.jpg", LocalDate.of(2025, 10, 26),
                LocalDate.of(2025, 10, 28), LocalDate.of(2025, 11, 4) ),
            Rent("Harry Potter e a Pedra Filosofal", "J.K. Rowling", "João Luiz","Recusado", "https://m.media-amazon.com/images/I/61jgm6ooXzL._AC_UF1000,1000_QL80_.jpg", LocalDate.of(2025, 10, 26),
                LocalDate.of(2025, 10, 28), LocalDate.of(2025, 11, 4) )
        )
        if (status == null) return emptyList()
        return todasSolicitacoes.filter { it.status.equals(status, ignoreCase = true) }
    }

    companion object {
        private const val ARG_STATUS_FILTER = "arg_status_filter"
        fun newInstance(statusFilter: String): RequestsFragment {
            val fragment = RequestsFragment()
            val args = Bundle().apply {
                putString(ARG_STATUS_FILTER, statusFilter)
            }
            fragment.arguments = args
            return fragment
        }
    }
}