/*
* @author Kambulat Alakaev (xalaka00)
* @brief A fragment that shows and sets the "Transactions" page
* */
package com.example.journalapp.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.journalapp.R
import com.example.journalapp.fragments.adapters.RecordAdapter
import com.example.journalapp.models.AppDB

class TransactionsFragment : Fragment() {
    private lateinit var view: View
    private lateinit var condition: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_transactions, container, false)

        val expBtn = view.findViewById<Button>(R.id.selectExpTransactions)
        val incBtn = view.findViewById<Button>(R.id.selectIncTransactions)

        val whiteClr = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
        val greyClr = ColorStateList.valueOf(Color.parseColor("#FFDEDEDE"))

        // set the default colors fo the transaction type buttons
        expBtn.backgroundTintList = whiteClr
        incBtn.backgroundTintList = greyClr

        var recTransType = "Expense"
        // create a basic condition depending on the type of transaction
        condition = "WHERE ${AppDB.KEY_TRANS_TYPE} = '$recTransType'"
        // select transactions of the selected type
        showTransactions(view,condition)

        //set white color to the background of the Expense button
        expBtn.setOnClickListener() {
            expBtn.backgroundTintList = whiteClr
            incBtn.backgroundTintList = greyClr

            recTransType = "Expense"
            condition = "WHERE ${AppDB.KEY_TRANS_TYPE} = '$recTransType'"
            showTransactions(view,condition)

        }
        //set white color to the background of the Income button
        incBtn.setOnClickListener() {
            incBtn.backgroundTintList = whiteClr
            expBtn.backgroundTintList = greyClr

            recTransType = "Income"
            condition = "WHERE ${AppDB.KEY_TRANS_TYPE} = '$recTransType'"
            showTransactions(view,condition)
        }

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

        // Set up the refresh listener
        swipeRefreshLayout.setOnRefreshListener {
            showTransactions(view,condition)
            // Signal that the refresh has finished
            swipeRefreshLayout.isRefreshing = false
        }
        return view
    }

    private fun showTransactions(view: View, condition: String) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewTransactions)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val dbHandler: AppDB = AppDB(requireContext())
        val records = dbHandler.viewTransactions(condition)
        val adapter = RecordAdapter(records)
        recyclerView.adapter = adapter
    }
    override fun onResume() {
        super.onResume()

        showTransactions(view,condition)
    }

}