/*
 * @author Zdebska Kateryna (xzdebs00)
 * @brief A fragment that shows and sets the "goals" page
 */

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

/**
 * A fragment that displays and manages the "Goals" page.
 */
class GoalsFragment : Fragment() {

    // Lazy initialization of SharedViewModel
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

        // Get reference to the "Add Goal" button
        val addGoalButton = view.findViewById<Button>(R.id.addGoalButton)

        // Set onClickListener for the "Add Goal" button
        addGoalButton.setOnClickListener() {
            // Create a new AddGoalFragment and open it
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val addGoalFragment = AddGoalFragment()

            fragmentTransaction.add(R.id.main_container, addGoalFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

            // Refresh the goals list after adding a new goal
            showGoals(view)
        }

        // Display the list of goals
        showGoals(view)

        // Set up the SwipeRefreshLayout
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayoutGoal)

        // Set up the refresh listener
        swipeRefreshLayout.setOnRefreshListener {

            // Refresh the goals list
            showGoals(view)

            // Signal that the refresh has finished
            swipeRefreshLayout.isRefreshing = false
        }

        return view
    }

    /**
     * Display the list of goals in the RecyclerView.
     */
    private fun showGoals(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewGoals)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        // Initialize the database handler
        val dbHandler: AppDB = AppDB(requireContext())

        // Retrieve the list of goals from the database
        val records = dbHandler.viewGoals()

        // Create and set up the GoalsAdapter with the list of goals
        val adapter = GoalsAdapter(records, sharedViewModel)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        // Refresh the goals list when the fragment resumes
        showGoals(view)
    }
}
