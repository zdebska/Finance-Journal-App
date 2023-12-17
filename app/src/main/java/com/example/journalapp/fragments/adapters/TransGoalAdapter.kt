/*
 * @author Zdebska Kateryna (xzdebs00)
 * @brief Bind data of a record from the "goal_transactions" table to the view of the main page
 */

package com.example.journalapp.fragments.adapters

import SharedViewModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.R
import com.example.journalapp.models.AppDB
import com.example.journalapp.models.GoalModel
import com.example.journalapp.models.GoalTransactionModel

/**
 * Adapter class to bind data of a record from the "goal_transactions" table to the view of the main page.
 *
 * @param goal The associated goal for which transactions are displayed.
 * @param transactions The list of goal transactions to be displayed.
 * @param sharedViewModel An instance of SharedViewModel for communication between fragments.
 */
class TransGoalAdapter(private val goal: GoalModel, private val transactions: MutableList<GoalTransactionModel>, private val sharedViewModel: SharedViewModel) : RecyclerView.Adapter<TransGoalAdapter.TransGoalViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransGoalViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_transaction_piece, parent, false)
        return TransGoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransGoalViewHolder, position: Int) {
        // Retrieve goal transaction data
        val trans = transactions[position]

        // Set goal transaction data to respective UI elements
        holder.transAmountText.text = trans.amount.toString() + " $"
        holder.transDateText.text = trans.creationDate

        val dbHandler: AppDB = AppDB(context)

        // Handle button click to delete goal transaction
        holder.deleteTransactionBtn.setOnClickListener {
            sharedViewModel.selectGoal(goal)
            val status = dbHandler.deleteGoalTrans(trans.id)
            if (status == 1) {
                // Remove the item from the list and then notify the adapter
                transactions.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    /**
     * ViewHolder class to represent each item in the RecyclerView.
     */
    inner class TransGoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transAmountText: TextView = itemView.findViewById(R.id.transAmountText)
        val transDateText: TextView = itemView.findViewById(R.id.transDateText)
        val deleteTransactionBtn: Button = itemView.findViewById(R.id.deleteTransactionBtn)
    }
}
