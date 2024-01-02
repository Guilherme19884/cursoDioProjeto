package com.example.dio1.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dio1.ui.CarFragment
import com.example.dio1.ui.MaisVendidosFragment

class TabAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> CarFragment();
            1 -> MaisVendidosFragment();
            else -> CarFragment();
        }
    }

    override fun getItemCount(): Int {
        return 2;
    }
}