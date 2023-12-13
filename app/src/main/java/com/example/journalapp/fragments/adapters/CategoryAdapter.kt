package com.example.journalapp.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.R
import com.example.journalapp.models.CategoryModel



class CategoryAdapter(
    private val categories: List<CategoryModel>,
    private val itemClickListener: OnCategoryItemClickListener
    ) : RecyclerView.Adapter<CategoryAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_record, parent, false)
        return CustomViewHolder(view)
    }

    interface OnCategoryItemClickListener {
        fun onCategoryItemClick(iconPath: String)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryName.text = category.name

        if(category.name == "Food") {
            holder.categoryIcon.setImageResource(R.drawable.baseline_food_bank_24)
        }
        else if (category.name == "Shopping") {
            holder.categoryIcon.setImageResource(R.drawable.baseline_shopping_cart_24)
        }
        else if (category.name == "Education") {
            holder.categoryIcon.setImageResource(R.drawable.baseline_school_24)
        }
        else if (category.name == "Taxes") {
            holder.categoryIcon.setImageResource(R.drawable.baseline_account_balance_24)
        }
        else if (category.name == "Salary") {
            holder.categoryIcon.setImageResource(R.drawable.baseline_attach_money_24)
        }
        else if (category.name == "Rent") {
            holder.categoryIcon.setImageResource(R.drawable.baseline_home_24)
        }
        else if (category.name == "Relax") {
            holder.categoryIcon.setImageResource(R.drawable.baseline_sports_tennis_24)
        }
        else {
            holder.categoryIcon.setImageResource(R.drawable.baseline_other_houses_24)
        }


        holder.itemView.setOnClickListener {
            itemClickListener.onCategoryItemClick(category.iconPath)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.catNoteText)
        val categoryIcon: ImageView = itemView.findViewById(R.id.catTypeIcon)
    }
}
