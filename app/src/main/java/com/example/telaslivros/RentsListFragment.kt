package com.example.telaslivros

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RentsListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var rentsAdapter: RentsAdapter

    // Variável para guardar o tipo de lista que este fragmento deve mostrar
    private var listType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Lemos o argumento que o PagerAdapter nos enviou
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

        // 2. Usamos o 'listType' para pegar os dados filtrados
        val mockData = criarComentariosMock(listType)

        recyclerView = view.findViewById(R.id.recyclerViewRents)
        rentsAdapter = RentsAdapter(mockData)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = rentsAdapter
    }

    // 3. ATUALIZAMOS A FUNÇÃO DE MOCK
    // Agora ela tem uma "base de dados" maior e filtra
    // de acordo com o tipo solicitado.
    private fun criarComentariosMock(tipo: String?): List<Rent> {
        val todosOsAlugueis = listOf(
            Rent("A Culpa é das Estrelas", "John Green", "Pendente"),
            Rent("O Pequeno Príncipe", "Antoine de Saint-Exupéry", "Pendente"),
            Rent("Duna", "Frank Herbert", "Aprovado"),
            Rent("O Senhor dos Anéis", "J.R.R. Tolkien", "Aprovado"),
            Rent("1984", "George Orwell", "Histórico"),
            Rent("Cem Anos de Solidão", "Gabriel García Márquez", "Histórico")
        )

        // Se o tipo for nulo, retorna uma lista vazia
        if (tipo == null) return emptyList()

        // Filtra a lista principal com base no status
        // (Exatamente como você faria no banco de dados com "WHERE status = ...")
        return when (tipo) {
            "PENDENTE" -> todosOsAlugueis.filter { it.status == "Pendente" }
            "APROVADO" -> todosOsAlugueis.filter { it.status == "Aprovado" }
            "HISTORICO" -> todosOsAlugueis.filter { it.status == "Histórico" }
            else -> emptyList()
        }
    }

    // 4. ESTE É O "COMPANION OBJECT"
    // É a "fábrica" padrão para criar fragmentos com argumentos de forma segura.
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