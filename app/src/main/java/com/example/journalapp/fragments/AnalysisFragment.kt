package com.example.journalapp.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.R
import com.example.journalapp.fragments.adapters.RecordAdapter
import com.example.journalapp.models.AppDB

class AnalysisFragment : Fragment() {
    private lateinit var spinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_analysis, container, false)

        val periods = resources.getStringArray(R.array.Period)
        spinner = view.findViewById(R.id.spinner)

        // Create an ArrayAdapter for the Spinner
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            periods
        )

        // Set the dropdown layout style
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the ArrayAdapter to the Spinner
        spinner.adapter = adapter

        // Set a selection listener for the Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = adapter.getItem(position).toString()
                // Handle the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected
            }
        }

        val expBtn = view.findViewById<Button>(R.id.selectExpTransactions)
        val incBtn = view.findViewById<Button>(R.id.selectIncTransactions)
        val whiteClr = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
        val greyClr = ColorStateList.valueOf(Color.parseColor("#FFDEDEDE"))

        expBtn.backgroundTintList = whiteClr
        incBtn.backgroundTintList = greyClr
        //set white color to the background of the Expense button
        expBtn.setOnClickListener() {
            expBtn.backgroundTintList = whiteClr
            incBtn.backgroundTintList = greyClr
        }
        //set white color to the background of the Income button
        incBtn.setOnClickListener() {
            incBtn.backgroundTintList = whiteClr
            expBtn.backgroundTintList = greyClr
        }
        return view
    }
    private fun showCategories(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val dbHandler: AppDB = AppDB(requireContext())
        val records = dbHandler.viewTransactions("ORDER BY ${AppDB.KEY_ID} DESC LIMIT 4")
        val adapter = RecordAdapter(records)
        recyclerView.adapter = adapter
    }


}