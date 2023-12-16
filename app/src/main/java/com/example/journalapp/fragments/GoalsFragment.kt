package com.example.journalapp.fragments

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
import com.example.journalapp.fragments.adapters.GoalsAdapter
import com.example.journalapp.models.AppDB


class GoalsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_goals, container, false)

        val addGoalButton = view.findViewById<Button>(R.id.addGoalButton)

        addGoalButton.setOnClickListener() {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val addGoalFragment = AddGoalFragment()

            fragmentTransaction.add(R.id.main_container, addGoalFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewGoals)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val dbHandler: AppDB = AppDB(requireContext())
        val records = dbHandler.viewGoals()
        val adapter = GoalsAdapter(records)
        recyclerView.adapter = adapter

        return view
    }

}