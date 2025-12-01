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

class RentsListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    // Inicializamos como null ou garantimos inicialização no onViewCreated
    private var rentsAdapter: RentsAdapter? = null
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
        // Infla o layout que contém apenas o RecyclerView
        return inflater.inflate(R.layout.fragment_rents_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewRents)
        recyclerView.layoutManager = LinearLayoutManager(context)



        rentsAdapter = RentsAdapter(emptyList())
        recyclerView.adapter = rentsAdapter



        loadRentsFromDatabase()
    }

    private fun loadRentsFromDatabase() {

        if (!isAdded) return

        val sharedPrefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userId = sharedPrefs.getInt("USER_ID_INT", 0)

        if (userId == 0) return

        lifecycleScope.launch(Dispatchers.IO) {
            // 3. Busca os dados no banco (Background)
            val allRents = DatabaseHelper.getAllRents(userId)

            val userRents = allRents.filter { it.userId == userId }

            val filteredList = when (listType) {
                "PENDENTE" -> userRents.filter { it.status == Status.PENDENTE }
                "APROVADO" -> userRents.filter { it.status == Status.APROVADO }
                "HISTORICO" -> userRents.filter {
                    it.status == Status.DEVOLVIDO ||
                            it.status == Status.RECUSADO
                }
                else -> emptyList()
            }


            withContext(Dispatchers.Main) {

                if (isAdded) {
                    rentsAdapter = RentsAdapter(filteredList)
                    recyclerView.adapter = rentsAdapter



                }
            }
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