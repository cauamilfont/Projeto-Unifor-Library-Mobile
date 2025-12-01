package com.example.telaslivros

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.text.toUpperCase
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.Locale

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
        // Infla o layout que contém APENAS o RecyclerView
        return inflater.inflate(R.layout.fragment_solicitacoes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvFragmentSolicitacoes)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 1. Inicializa com lista vazia para evitar erros antes do banco responder
        adapter = ManageRequestAdapter(emptyList())
        recyclerView.adapter = adapter

        // 2. Carrega os dados
        loadRequestsFromDatabase()
    }

    override fun onResume() {
        super.onResume()
        // Recarrega os dados quando voltar da tela de detalhes (caso tenha aprovado algo)
        loadRequestsFromDatabase()
    }

    private fun loadRequestsFromDatabase() {
        if (!isAdded) return // Segurança

        lifecycleScope.launch(Dispatchers.IO) {
            // 1. Busca TODOS os aluguéis (Visão do Admin)
            // Certifique-se que seu DatabaseHelper tem essa função
            val allRents = DatabaseHelper.getAllRents(0)

            // 2. Filtra baseado na aba atual (Pendente, Aprovado, Recusado)
            val filteredList = if (statusFilter != null) {
                allRents.filter {
                    it.status == Status.valueOf(statusFilter!!.uppercase(Locale.ROOT))
                }
            } else {
                emptyList()
            }

            // 3. Atualiza a UI na Thread Principal
            withContext(Dispatchers.Main) {
                if (isAdded) {
                    adapter = ManageRequestAdapter(filteredList)
                    recyclerView.adapter = adapter
                }
            }
        }
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