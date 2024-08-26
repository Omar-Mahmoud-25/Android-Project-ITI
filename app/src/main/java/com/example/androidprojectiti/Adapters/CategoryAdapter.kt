package com.example.androidprojectiti.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidprojectiti.R
import com.example.androidprojectiti.dto.Category

class CategoryAdapter(val listOfOfCategories:List<Category>): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    class ViewHolder(val row: View):RecyclerView.ViewHolder(row) {
        var title: TextView = row.findViewById(R.id.CategoryName)
        var thumbnail: ImageView = row.findViewById(R.id.CayegoryImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout= LayoutInflater.from(parent.context).inflate(R.layout.category_list_item,parent,false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return listOfOfCategories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text=listOfOfCategories[position].strCategory
        Glide.with(holder.thumbnail.context)
            .load(listOfOfCategories[position].strCategoryThumb)
            .placeholder(R.drawable.baseline_arrow_circle_down_24)
            .error(R.drawable.baseline_error_24)
            .into(holder.thumbnail)
    }
}