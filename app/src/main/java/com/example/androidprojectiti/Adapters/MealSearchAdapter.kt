package com.example.myapplicationrecyclarview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidprojectiti.R
import com.example.androidprojectiti.dto.MealResponse.Meal


class MealSearchAdapter(private var meals: List<Meal>) : RecyclerView.Adapter<MealSearchAdapter.ViewHolder>() {

    // Create a new ViewHolder instance
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meal_search_layout, parent, false)
        return ViewHolder(view)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = meals[position]
        holder.nameTextView.text = meal.strMeal
        holder.areaTextView.text = "${meal.strArea} Meal"
        holder.categoryTextView.text = meal.strCategory

        // Load image into the ImageView using Glide
        Glide.with(holder.itemView.context)
            .load(meal.strMealThumb)
            .placeholder(R.drawable.ic_launcher_foreground) // Placeholder image
            .into(holder.imageView)
    }

    // Return the total number of meals
    override fun getItemCount(): Int = meals.size

    // Method to update data
    fun updateData(newItems: List<Meal>) {
        meals = newItems
        notifyDataSetChanged() // Notify the adapter that data has changed
    }

    // ViewHolder class to hold references to the views
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.item_name)
        val areaTextView: TextView = itemView.findViewById(R.id.item_area)
        val categoryTextView: TextView = itemView.findViewById(R.id.item_category)
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
    }
}
