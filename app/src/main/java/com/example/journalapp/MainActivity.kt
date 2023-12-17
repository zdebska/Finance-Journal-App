package com.example.journalapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.example.journalapp.fragments.AddGoalFragment
import com.example.journalapp.fragments.AddTransactionFragment
import com.example.journalapp.fragments.GoalsFragment
import com.example.journalapp.fragments.HomeFragment
import com.example.journalapp.fragments.TransactionsFragment
import com.example.journalapp.fragments.adapters.ViewPagerAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private val viewPager:ViewPager
        get() = findViewById(R.id.viewPager)
    private val tabs: TabLayout
        get() = findViewById(R.id.tabs)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpTabs()
    }
    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment(), "Home")
        adapter.addFragment(TransactionsFragment(), "Transactions")
        adapter.addFragment(GoalsFragment(), "Goals")

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        // Assign icons to fragments
        tabs.getTabAt(0)?.setIcon(R.drawable.baseline_home_24)
        tabs.getTabAt(1)?.setIcon(R.drawable.baseline_currency_exchange_24)
        tabs.getTabAt(2)?.setIcon(R.drawable.baseline_savings_24)
    }

    // Function that changes the selected tab in a TabLayout to the one specified by the index.
    fun changeTab(index: Int) {
        tabs.getTabAt(index)?.select()
    }
}
