/*
 * @author Zdebska Kateryna (xzdebs00)
 * @brief Adapter class to bind data of a record from the "goals" table to the view of the main page.
 */

package com.example.journalapp.fragments.adapters

import SharedViewModel
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.R
import com.example.journalapp.fragments.AddGoalTransFragment
import com.example.journalapp.fragments.GoalTransFragment
import com.example.journalapp.models.AppDB
import com.example.journalapp.models.GoalModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Adapter class to bind data of a record from the "goals" table to the view of the main page.
 *
 * @param goals The list of goals to be displayed.
 * @param sharedViewModel An instance of SharedViewModel for communication between fragments.
 */
class GoalsAdapter(private val goals: List<GoalModel>, private val sharedViewModel: SharedViewModel) : RecyclerView.Adapter<GoalsAdapter.GoalViewHolder>() {

    private lateinit var context: Context

    /**
     * Creates a new GoalViewHolder instance when needed.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_piece, parent, false)
        return GoalViewHolder(view)
    }

    /**
     * Binds the data of a goal to the UI elements of the view.
     */
    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        // Retrieve goal data
        val goal = goals[position]

        // Set goal data to respective UI elements
        holder.goalNameText.text = goal.name
        holder.goalAmountText.text = goal.amount.toString() + " $"
        holder.goalEndDateText.text = goal.endDate

        // Parse end date and compare with the current date
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val endDate = dateFormat.parse(goal.endDate)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val currentDateWithoutTime = Date(calendar.timeInMillis)

        // Change text color to red if the deadline has passed
        if ((goal.isReached == 0) and (currentDateWithoutTime > endDate)){
            holder.goalEndDateText.setTextColor(Color.RED)
        }

        // Calculate and display saved amount
        val dbHandler: AppDB = AppDB(context)
        val saved = dbHandler.calculateSavedAmountForGoal(goal.id)
        holder.goalSavedText.text = saved.toString() + " $"

        // Adjust UI elements based on saved percentage
        if ((100 * saved / goal.amount).toInt() >= 100) {
            holder.buttonAddGoalTransaction.visibility = View.GONE
            holder.buttonAddGoalTransaction.isClickable = false
            holder.textAddGoalTransaction.visibility = View.VISIBLE
        }

        // Set progress bar and percentage text
        if ((100 * saved / goal.amount).toInt() > 100){
            holder.progressBar.progress = 100
            holder.goalPercentText.text = ">100%"
        } else {
            holder.progressBar.progress = (100 * saved / goal.amount).toInt()
            holder.goalPercentText.text = (100 * saved / goal.amount).toInt().toString() + "%"
        }

        // Handle item click to navigate to GoalTransFragment
        holder.itemView.setOnClickListener {
            sharedViewModel.selectGoal(goal)
            val fragment = GoalTransFragment.newInstance(goal)
            val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, fragment, GoalTransFragment::class.java.simpleName)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        // Handle button click to navigate to AddGoalTransFragment
        holder.buttonAddGoalTransaction.setOnClickListener {
            sharedViewModel.selectGoal(goal)
            val fragment = AddGoalTransFragment.newInstance(goal)
            val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, fragment, GoalTransFragment::class.java.simpleName)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    /**
     * Returns the total number of goals in the adapter.
     */
    override fun getItemCount(): Int {
        return goals.size
    }

    /**
     * ViewHolder class to hold and manage the UI elements for each goal item.
     */
    inner class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalNameText: TextView = itemView.findViewById(R.id.goalNameText)
        val goalAmountText: TextView = itemView.findViewById(R.id.goalAmountText)
        val goalEndDateText: TextView = itemView.findViewById(R.id.goalEndDateText)
        val goalSavedText: TextView = itemView.findViewById(R.id.goalSavedAmountText)
        val progressBar: ProgressBar = itemView.findViewById(R.id.my_progressBar)
        val goalPercentText: TextView = itemView.findViewById(R.id.goalPercentText)
        val buttonAddGoalTransaction: Button = itemView.findViewById(R.id.buttonAddGoalTransaction)
        val textAddGoalTransaction: TextView = itemView.findViewById(R.id.textAddGoalTransaction)
    }
}
