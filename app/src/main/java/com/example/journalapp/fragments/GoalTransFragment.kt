/*
 * @author Zdebska Kateryna (xzdebs00)
 * @brief A fragment that displays and manages the "goal" page.
 */

package com.example.journalapp.fragments

import SharedViewModel
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.journalapp.R
import com.example.journalapp.fragments.adapters.TransGoalAdapter
import com.example.journalapp.models.AppDB
import com.example.journalapp.models.GoalModel
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * A fragment that displays and manages the "goal" page.
 */
class GoalTransFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by lazy {
        SharedViewModel.getInstance(requireActivity().application)
    }
    private lateinit var view: View

    companion object {
        private const val ARG_DATA = "data"

        /**
         * Creates a new instance of GoalTransFragment with the given GoalModel data.
         */
        fun newInstance(data: GoalModel): GoalTransFragment {
            val fragment = GoalTransFragment()
            val args = Bundle()
            args.putParcelable(ARG_DATA, data)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_goal_trans, container, false)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayoutGoal)
        val record: GoalModel? = arguments?.getParcelable(GoalTransFragment.ARG_DATA)

        // Set up the refresh listener
        swipeRefreshLayout.setOnRefreshListener {
            showGoalTrans(view)
            // Signal that the refresh has finished
            swipeRefreshLayout.isRefreshing = false
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val record: GoalModel? = arguments?.getParcelable(GoalTransFragment.ARG_DATA)

        // Show the goal details and transactions
        showGoal(record)
        sharedViewModel.selectedGoal.observe(viewLifecycleOwner) { goal ->
            showGoal(goal)
            showGoalTrans(view)
        }
    }

    /**
     * Displays the details of the selected goal.
     */
    private fun showGoal(record: GoalModel?) {

        val dbHandler: AppDB = AppDB(requireContext())

        val nameGoal = view.findViewById<MaterialTextView>(R.id.nameGoal)
        val amountGoal = view.findViewById<MaterialTextView>(R.id.goalAmountText)
        val savedAmountGoal = view.findViewById<MaterialTextView>(R.id.goalSavedAmountText)
        val endDateGoal = view.findViewById<MaterialTextView>(R.id.goalEndDateText)
        val arrowBackBtn = view.findViewById<ImageView>(R.id.arrowBackBtn)
        val deleteBtn = view.findViewById<ImageView>(R.id.deleteBtn)
        val editBtn = view.findViewById<ImageView>(R.id.editBtn)
        val addTrans = view.findViewById<Button>(R.id.buttonAddGoalTransaction)
        val addTransText = view.findViewById<TextView>(R.id.textAddGoalTransaction)

        // Set the goal name to the Button or TextView as needed
        nameGoal.text = record!!.name.toString()
        amountGoal.text = record!!.amount.toString() + " $"
        endDateGoal.text = record!!.endDate

        val saved = dbHandler.calculateSavedAmountForGoal(record!!.id)
        savedAmountGoal.text = saved.toString() + " $"

        // Adjust UI elements based on saved percentage
        if ((100 * saved / record.amount).toInt() >= 100) {
            addTrans.visibility = View.GONE
            addTrans.isClickable = false
            addTransText.visibility = View.VISIBLE
        } else {
            addTrans.visibility = View.VISIBLE
            addTrans.isClickable = true
            addTransText.visibility = View.GONE
        }

        val dateFormat =
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Adjust the pattern accordingly
        val endDate = dateFormat.parse(record.endDate)

        // Check if today's date is before the deadline
        val calendar = Calendar.getInstance()
        // Set the time components of currentDate to midnight
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val currentDateWithoutTime = Date(calendar.timeInMillis)
        if ((record!!.isReached == 0) and (currentDateWithoutTime > endDate)) {
            endDateGoal.setTextColor(Color.RED) // Set text color to red
        }

        arrowBackBtn.setOnClickListener {
            // Pop the fragment from the back stack to return to the previous fragment
            requireActivity().supportFragmentManager.popBackStack()
        }

        deleteBtn.setOnClickListener {
            // Delete the goal and pop the fragment from the back stack
            val status = dbHandler.deleteGoal(record!!.id)
            if (status == 1) {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        editBtn.setOnClickListener {
            // Open the EditGoalFragment for editing the goal
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val editGoal = EditGoalFragment.newInstance(record)
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(
                R.id.main_container,
                editGoal,
                EditGoalFragment::class.java.simpleName
            )
            transaction.addToBackStack(null)
            transaction.commit()
        }

        addTrans.setOnClickListener {
            // Open the AddGoalTransFragment to add a new goal transaction
            sharedViewModel.selectGoal(record)
            val fragment = AddGoalTransFragment.newInstance(record)
            val fragmentManager = (requireActivity() as AppCompatActivity).supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(
                R.id.main_container,
                fragment,
                AddGoalTransFragment::class.java.simpleName
            )
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    /**
     * Displays the list of transactions for the selected goal.
     */
    private fun showGoalTrans(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewGoalTrans)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val dbHandler: AppDB = AppDB(requireContext())
        val record: GoalModel? = arguments?.getParcelable(GoalTransFragment.ARG_DATA)
        val records = dbHandler.viewGoalsTransactions(record!!.id)
        val adapter = TransGoalAdapter(record, records, sharedViewModel)
        recyclerView.adapter = adapter
    }
}
