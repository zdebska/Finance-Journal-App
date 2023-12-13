package com.example.journalapp

import android.content.Intent
import android.os.Bundle
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

    private lateinit var toggle: ActionBarDrawerToggle
    private val mainContainer:DrawerLayout
        get() = findViewById(R.id.main_container)
    private val viewPager:ViewPager
        get() = findViewById(R.id.viewPager)
    private val tabs: TabLayout
        get() = findViewById(R.id.tabs)
    private val toolbar: MaterialToolbar
        get() = findViewById(R.id.toolbar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, mainContainer, toolbar, R.string.open, R.string.close)
        mainContainer.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(false) // Hide the default back arrow

        setUpTabs()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                mainContainer.openDrawer(GravityCompat.START)
                return true
            }
            R.id.menu_item1 -> {
                val addGoalFragment = AddGoalFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, addGoalFragment)
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.menu_item2 -> {
                // Handle the press event for menu item 2
                // Do something when the user selects menu item 2
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState() // Sync the state after onRestoreInstanceState has occurred.
    }

    override fun onSupportNavigateUp(): Boolean {
        mainContainer.openDrawer(GravityCompat.START) // Open the drawer when the home button is pressed
        return true
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
        tabs.getTabAt(2)?.setIcon(R.drawable.baseline_bar_chart_24)
    }

    // Function that changes the selected tab in a TabLayout to the one specified by the index.
    fun changeTab(index: Int) {
        tabs.getTabAt(index)?.select()
    }
}
