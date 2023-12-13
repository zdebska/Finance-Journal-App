package com.example.journalapp.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.R

// ColorAdapter.kt
class ColorAdapter(private val colors: List<Int>,
                   private val onColorItemClickListener: OnColorItemClickListener) :
    RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {
    interface OnColorItemClickListener {
        fun onColorItemClick(colorResId: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.color_item, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val colorResId = colors[position]
        holder.bind(colorResId)
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val colorView: View = itemView.findViewById(R.id.colorView)
        fun bind(colorResId: Int) {
            colorView.setBackgroundResource(colorResId)
            // Set click listener to handle color item click
            itemView.setOnClickListener {
                onColorItemClickListener.onColorItemClick(colorResId)
            }
        }
    }
}
