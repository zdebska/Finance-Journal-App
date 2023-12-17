package com.example.journalapp.fragments.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.R
import com.example.journalapp.fragments.EditTransactionFragment
import com.example.journalapp.fragments.GoalTransFragment
import com.example.journalapp.models.AppDB
import com.example.journalapp.models.GoalModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

class GoalsAdapter(private val goals: List<GoalModel>) : RecyclerView.Adapter<GoalsAdapter.GoalViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_piece, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goals[position]

        holder.goalNameText.text = goal.name
        holder.goalAmountText.text = goal.amount.toString() + " $"
        holder.goalEndDateText.text = goal.endDate
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Adjust the pattern accordingly
        val endDate = dateFormat.parse(goal.endDate)

        // Check if today's date is before the deadline
        val calendar = Calendar.getInstance()
        // Set the time components of currentDate to midnight
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val currentDateWithoutTime = Date(calendar.timeInMillis)
        if ((goal.isReached == 0) and (currentDateWithoutTime > endDate)){
            holder.goalEndDateText.setTextColor(Color.RED) // Set text color to red
            //holder.textAddGoalTransaction.visibility = View.GONE
        }
        val dbHandler: AppDB = AppDB(context)
        val saved = dbHandler.calculateSavedAmountForGoal(goal.id)
        holder.goalSavedText.text = saved.toString()
        holder.progressBar.progress = (100 * saved / goal.amount).toInt()
        holder.goalPercentText.text = (100 * saved / goal.amount).toInt().toString()+ "%"
        holder.itemView.setOnClickListener {
            val fragment = GoalTransFragment.newInstance(goal)
            val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, fragment, GoalTransFragment::class.java.simpleName)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun getItemCount(): Int {
        return goals.size
    }

    inner class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalNameText: TextView = itemView.findViewById(R.id.goalNameText)
        val goalAmountText: TextView = itemView.findViewById(R.id.goalAmountText)
        val goalEndDateText: TextView = itemView.findViewById(R.id.goalEndDateText)
        val goalSavedText: TextView = itemView.findViewById(R.id.goalSavedAmountText)
        val progressBar: ProgressBar = itemView.findViewById(R.id.my_progressBar)
        val goalPercentText: TextView = itemView.findViewById(R.id.goalPercentText)
        //val textAddGoalTransaction: TextView = itemView.findViewById(R.id.textAddGoalTransaction)
    }



}
