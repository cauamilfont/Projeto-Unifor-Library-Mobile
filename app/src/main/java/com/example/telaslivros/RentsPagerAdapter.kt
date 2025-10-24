package com.example.telaslivros

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class RentsPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    // 3 abas
    override fun getItemCount(): Int = 3

    // Cria o fragmento para a posição da aba
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RentsListFragment.newInstance("PENDENTE")  // Para a aba "Em Análise"
            1 -> RentsListFragment.newInstance("APROVADO")  // Para a aba "Aprovados"
            2 -> RentsListFragment.newInstance("HISTORICO") // Para a aba "Histórico"
            else -> throw IllegalStateException("Posição inválida")
        }
    }
}