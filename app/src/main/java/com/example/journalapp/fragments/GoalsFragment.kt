package com.example.journalapp.fragments

import SharedViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.journalapp.R
import com.example.journalapp.fragments.adapters.GoalsAdapter
import com.example.journalapp.models.AppDB


class GoalsFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by lazy {
        SharedViewModel.getInstance(requireActivity().application)
    }

    private lateinit var view: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        view = inflater.inflate(R.layout.fragment_goals, container, false)

        val addGoalButton = view.findViewById<Button>(R.id.addGoalButton)

        addGoalButton.setOnClickListener() {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val addGoalFragment = AddGoalFragment()

            fragmentTransaction.add(R.id.main_container, addGoalFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            showGoals(view)
        }
        showGoals(view)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayoutGoal)

        // Set up the refresh listener
        swipeRefreshLayout.setOnRefreshListener {

            showGoals(view)
            // Signal that the refresh has finished
            swipeRefreshLayout.isRefreshing = false
        }

        return view
    }
    private fun showGoals(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewGoals)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val dbHandler: AppDB = AppDB(requireContext())
        val records = dbHandler.viewGoals()
        val adapter = GoalsAdapter(records, sharedViewModel)
        recyclerView.adapter = adapter

    }
    override fun onResume() {
        super.onResume()
        showGoals(view)
    }

}