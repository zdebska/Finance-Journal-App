package com.example.journalapp.fragments.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.R
import com.example.journalapp.models.CategoryModel



class CategoryAdapter(
    private val categories: List<CategoryModel>,
    private val itemClickListener: OnCategoryItemClickListener
    ) : RecyclerView.Adapter<CategoryAdapter.CustomViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_record, parent, false)
        return CustomViewHolder(view)
    }

    interface OnCategoryItemClickListener {
        fun onCategoryItemClick(iconPath: String)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryName.text = category.name
        holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, category.colorResId))
        val resourceId = getResourceId(category.iconPath)
        holder.categoryIcon.setImageResource(resourceId)



        holder.itemView.setOnClickListener {
            itemClickListener.onCategoryItemClick(category.iconPath)
        }
    }
    private fun getResourceId(iconPath: String): Int {
        // Convert iconPath to resource ID using resources
        return context.resources.getIdentifier(iconPath, "drawable", context.packageName)
    }
    override fun getItemCount(): Int {
        return categories.size
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.catNoteText)
        val categoryIcon: ImageView = itemView.findViewById(R.id.catTypeIcon)
    }
}
