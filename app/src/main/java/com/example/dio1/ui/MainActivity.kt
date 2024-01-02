package com.example.dio1.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.dio1.R
import com.example.dio1.data.CarFactory
import com.example.dio1.ui.adapter.CarAdapter
import com.example.dio1.ui.adapter.TabAdapter
import com.google.android.material.tabs.TabLayout


lateinit var tabLayout: TabLayout;
lateinit var viewPager: ViewPager2;



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews();
        setupListeners();
        setupTabs();
    }

    fun setupViews(){
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.vp_viewPage);
    }

    fun setupListeners() {
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let{
                    viewPager.currentItem = it.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position)?.select();
            }
        })
    }

    fun setupTabs(){
        val tabsAdapter = TabAdapter(this);
        viewPager.adapter = tabsAdapter;
    }

}
