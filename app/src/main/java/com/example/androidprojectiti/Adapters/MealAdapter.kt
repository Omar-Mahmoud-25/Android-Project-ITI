package com.example.androidprojectiti.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidprojectiti.Adapters.CategoryAdapter.ViewHolder
import com.example.androidprojectiti.R
import com.example.androidprojectiti.dto.CategoryResponse.Category
import com.example.androidprojectiti.dto.MealResponse.Meal

class MealAdapter(val listOfOfMeals:List<Meal>): RecyclerView.Adapter<MealAdapter.ViewHolder>() {
    class ViewHolder(val row: View):RecyclerView.ViewHolder(row) {
        var name: TextView = row.findViewById(R.id.Name)
        var thumbnail: ImageView = row.findViewById(R.id.imageView)
        var category:TextView=row.findViewById(R.id.CategoryName)
        var favourite:ImageButton=row.findViewById(R.id.heart_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealAdapter.ViewHolder {
        val layout= LayoutInflater.from(parent.context).inflate(R.layout.meal_list_item,parent,false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text=listOfOfMeals[position].strMeal
        holder.category.text=listOfOfMeals[position].strCategory

        Glide.with(holder.thumbnail.context)
            .load(listOfOfMeals[position].strMealThumb)
            .placeholder(R.drawable.baseline_arrow_circle_down_24)
            .error(R.drawable.baseline_error_24)
            .into(holder.thumbnail)
        var isHeartRed = false

        holder.favourite.setOnClickListener {
            if (isHeartRed) {
                holder.favourite.setImageResource(R.drawable.white_heart)
            } else {
                holder.favourite.setImageResource(R.drawable.red_heart)
            }
            isHeartRed = !isHeartRed
        }
    }

    override fun getItemCount(): Int {
        return listOfOfMeals.size
    }


}