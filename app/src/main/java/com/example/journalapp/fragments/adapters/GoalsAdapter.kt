package com.example.journalapp.fragments.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.R
import com.example.journalapp.models.GoalModel

class GoalsAdapter(private val goals: List<GoalModel>) : RecyclerView.Adapter<GoalsAdapter.GoalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_piece, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goals[position]

        holder.goalNameText.text = goal.name
        holder.goalAmountText.text = goal.amount.toString() + " $"
        holder.goalEndDateText.text = goal.endDate
        //saved???
    }

    override fun getItemCount(): Int {
        return goals.size
    }

    inner class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalNameText: TextView = itemView.findViewById(R.id.goalNameText)
        val goalAmountText: TextView = itemView.findViewById(R.id.goalAmountText)
        val goalEndDateText: TextView = itemView.findViewById(R.id.goalEndDateText)
    }
}
