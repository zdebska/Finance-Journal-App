/*
* @author Assatulla Dias (xassat00)
* @author Alakaev Kambulat (xalaka00)
* @brief Home Fragment implementation
* */

package com.example.journalapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.MainActivity
import com.example.journalapp.R
import com.example.journalapp.fragments.adapters.RecordAdapter
import com.example.journalapp.models.AppDB
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class HomeFragment : Fragment() {
    private lateinit var view: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false)

        val mainActivity = requireActivity() as MainActivity
        val textSeeAll = view.findViewById<TextView>(R.id.textSeeAll)
        val textBalance = view.findViewById<TextView>(R.id.textBalance)
        val addButton = mainActivity.findViewById<Button>(R.id.addButton)


        textSeeAll.setOnClickListener {
            mainActivity.changeTab(1)
        }

        addButton.setOnClickListener() {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val addTransactionFragment = AddTransactionFragment()

            fragmentTransaction.add(R.id.main_container, addTransactionFragment)
            fragmentTransaction.addToBackStack("fragmentTransaction")
            fragmentTransaction.commit()
        }

        textBalance.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val balanceInfoFragment = BalanceInfoFragment()

            fragmentTransaction.add(R.id.main_container, balanceInfoFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

        // Set up the refresh listener
        swipeRefreshLayout.setOnRefreshListener {
            showLastTransactions(view)
            // Signal that the refresh has finished
            swipeRefreshLayout.isRefreshing = false
        }

        showLastTransactions(view)
        return view
    }

    // show last transactions on the main page
    // @author Alakaev Kambulat (xalaka00)
    private fun showLastTransactions(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val dbHandler: AppDB = AppDB(requireContext())
        val records = dbHandler.viewTransactions("ORDER BY ${AppDB.KEY_ID} DESC LIMIT 4")
        val adapter = RecordAdapter(records)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        showLastTransactions(view)
    }
}