/*
* @author Alakaev Kambulat (xalaka00)
* @brief Bind data of a record from the "transactions" table to the view of the main page
* */
package com.example.journalapp.fragments.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.R
import com.example.journalapp.fragments.EditTransactionFragment
import com.example.journalapp.models.TransactionModel

class RecordAdapter(private val records: List<TransactionModel>) : RecyclerView.Adapter<RecordAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_menu_record, parent, false)
        return CustomViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val record = records[position]
        holder.transTypeText.text = record.transType
        holder.transNoteText.text = record.note
        holder.transAmountText.text = record.amount.toString() + " $"

        if(record.transType == "Expense"){
        holder.transTypeIcon.setImageResource(R.drawable.baseline_arrow_circle_up_24)
        }
        else {
            holder.transTypeIcon.setImageResource(R.drawable.baseline_arrow_circle_down_24)
        }

        holder.itemView.setOnClickListener {
            val fragment = EditTransactionFragment.newInstance(record)
            val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, fragment, EditTransactionFragment::class.java.simpleName)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun getItemCount(): Int {
        return records.size
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transTypeText: TextView = itemView.findViewById(R.id.transTypeText)
        val transNoteText: TextView = itemView.findViewById(R.id.transNoteText)
        val transAmountText: TextView = itemView.findViewById(R.id.transAmountText)
        val transTypeIcon : ImageView = itemView.findViewById(R.id.transTypeIcon)
    }
}
