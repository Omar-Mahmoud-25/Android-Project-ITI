package com.example.myapplicationrecyclarview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidprojectiti.R
import com.example.androidprojectiti.dto.Meal


class MealSearchAdapter(private var items: List<Meal>) : RecyclerView.Adapter<MealSearchAdapter.ViewHolder>() {

    // Create a new ViewHolder instance
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meal_search_layout, parent, false)
        return ViewHolder(view)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
//        holder.nameTextView.text = item.name
//        holder.descriptionTextView.text = item.description
//        holder.categoryTextView.text = item.category

        // Load image into the ImageView using Glide
//        Glide.with(holder.itemView.context)
//            .load(item.imageResId)
//            .placeholder(R.drawable.ic_launcher_foreground) // Placeholder image
//            .into(holder.imageView)
    }

    // Return the total number of items
    override fun getItemCount(): Int = items.size

    // Method to update data
    fun updateData(newItems: List<Meal>) {
        items = newItems
        notifyDataSetChanged() // Notify the adapter that data has changed
    }

    // ViewHolder class to hold references to the views
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.item_name)
        val descriptionTextView: TextView = itemView.findViewById(R.id.item_description)
        val categoryTextView: TextView = itemView.findViewById(R.id.item_category)
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
    }
}
