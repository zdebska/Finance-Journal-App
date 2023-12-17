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
import com.example.journalapp.models.GoalTransactionModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TransGoalAdapter(private val goal: GoalModel, private val transactions: MutableList<GoalTransactionModel>, private val sharedViewModel: SharedViewModel) : RecyclerView.Adapter<TransGoalAdapter.TransGoalViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransGoalViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_transaction_piece, parent, false)
        return TransGoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransGoalViewHolder, position: Int) {
        val trans = transactions[position]

        holder.transAmountText.text = trans.amount.toString() + " $"
        holder.transDateText.text = trans.creationDate

        val dbHandler: AppDB = AppDB(context)

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

    inner class TransGoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transAmountText: TextView = itemView.findViewById(R.id.transAmountText)
        val transDateText: TextView = itemView.findViewById(R.id.transDateText)
        val deleteTransactionBtn: Button = itemView.findViewById(R.id.deleteTransactionBtn)

    }
}