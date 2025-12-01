package com.example.telaslivros

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class HistoryRentsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryRentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rents_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewRents)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 1. Inicializa com lista vazia para evitar "UninitializedPropertyAccessException"
        adapter = HistoryRentsAdapter(emptyList())
        recyclerView.adapter = adapter

        // 2. Carrega os dados reais do banco
        loadHistoryFromDatabase()
    }

    private fun loadHistoryFromDatabase() {
        if (!isAdded) return // Segurança caso o fragmento já tenha fechado

        val sharedPrefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userId = sharedPrefs.getInt("USER_ID_INT", 0)

        if (userId == 0) return

        lifecycleScope.launch(Dispatchers.IO) {
            // Busca TODAS as reservas (supondo que getAllRents traz tudo)
            val allRents = DatabaseHelper.getAllRents(userId)

            val historyList = allRents.filter { rent ->
                rent.status.equals(Status.DEVOLVIDO)
            }


            withContext(Dispatchers.Main) {
                if (isAdded) {
                    // Atualiza o adapter com a lista filtrada
                    adapter = HistoryRentsAdapter(historyList)
                    recyclerView.adapter = adapter
                }
            }
        }
    }

    companion object {
        fun newInstance(): HistoryRentsFragment {
            return HistoryRentsFragment()
        }
    }
}