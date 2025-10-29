package com.example.telaslivros

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate

class RentsListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var rentsAdapter: RentsAdapter


    private var listType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            listType = it.getString(ARG_LIST_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rents_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val mockData = criarComentariosMock(listType)

        recyclerView = view.findViewById(R.id.recyclerViewRents)
        rentsAdapter = RentsAdapter(mockData)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = rentsAdapter
    }


    private fun criarComentariosMock(tipo: String?): List<Rent> {
        val todosOsAlugueis = listOf(
            Rent("A Culpa é das Estrelas", "John Green", "João Luiz","Pendente", "https://m.media-amazon.com/images/I/811ivBP1rsL._UF1000,1000_QL80_.jpg", LocalDate.of(2025, 10, 26),
                LocalDate.of(2025, 10, 28), LocalDate.of(2025, 11, 4) ),
            Rent("O Pequeno Príncipe", "Antoine de Saint-Exupéry", "João Luiz", "Pendente", "https://m.media-amazon.com/images/I/81TmOZIXvzL._UF1000,1000_QL80_.jpg", LocalDate.of(2025, 10, 26),
                LocalDate.of(2025, 10, 28), LocalDate.of(2025, 11, 4) ),
            Rent("Duna", "Frank Herbert", "João Luiz","Aprovado", "https://m.media-amazon.com/images/I/81zN7udGRUL.jpg", LocalDate.of(2025, 10, 26),
                LocalDate.of(2025, 10, 28), LocalDate.of(2025, 11, 4) ),
            Rent("O Senhor dos Anéis", "J.R.R. Tolkien", "João Luiz","Aprovado", "https://m.media-amazon.com/images/I/71ZLavBjpRL._AC_UF1000,1000_QL80_.jpg", LocalDate.of(2025, 10, 26),
                LocalDate.of(2025, 10, 28), LocalDate.of(2025, 11, 4) ),
            Rent("1984", "George Orwell", "João Luiz","Histórico", "https://m.media-amazon.com/images/I/61t0bwt1s3L._AC_UF1000,1000_QL80_.jpg", LocalDate.of(2025, 10, 26),
                LocalDate.of(2025, 10, 28), LocalDate.of(2025, 11, 4) ),
            Rent("Harry Potter e a Pedra Filosofal", "J.K. Rowling", "João Luiz","Histórico", "https://m.media-amazon.com/images/I/61jgm6ooXzL._AC_UF1000,1000_QL80_.jpg", LocalDate.of(2025, 10, 26),
                LocalDate.of(2025, 10, 28), LocalDate.of(2025, 11, 4) )
        )


        if (tipo == null) return emptyList()

        return when (tipo) {
            "PENDENTE" -> todosOsAlugueis.filter { it.status == "Pendente" }
            "APROVADO" -> todosOsAlugueis.filter { it.status == "Aprovado" }
            "HISTORICO" -> todosOsAlugueis.filter { it.status == "Histórico" }
            else -> emptyList()
        }
    }


    companion object {
        private const val ARG_LIST_TYPE = "arg_list_type"

        fun newInstance(listType: String): RentsListFragment {
            val fragment = RentsListFragment()
            val args = Bundle().apply {
                putString(ARG_LIST_TYPE, listType)
            }
            fragment.arguments = args
            return fragment
        }
    }
}