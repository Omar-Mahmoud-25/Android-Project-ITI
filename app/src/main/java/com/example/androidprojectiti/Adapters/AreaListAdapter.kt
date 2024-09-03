package com.example.androidprojectiti.Adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidprojectiti.R
import com.example.androidprojectiti.dto.AreaResponse.Area

class AreaListAdapter(var listOfOfAreas:List<Area> = listOf(), val navController: NavController, val list_of_pictures:List<String>): RecyclerView.Adapter<AreaListAdapter.ViewHolder>() {
    class ViewHolder(val row: View):RecyclerView.ViewHolder(row) {
        var title: TextView = row.findViewById(R.id.area_name)
        var thumbnail: ImageView = row.findViewById(R.id.CayegoryImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout= LayoutInflater.from(parent.context).inflate(R.layout.area_list_item,parent,false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return listOfOfAreas.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text=listOfOfAreas[position].strArea
        Glide.with(holder.thumbnail.context)
            .load(list_of_pictures[position])
            .placeholder(R.drawable.baseline_arrow_circle_down_24)
            .error(R.drawable.baseline_error_24)
            .into(holder.thumbnail)

        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putString("area", listOfOfAreas[position].strArea)
            }
//            navController.navigate(R.id.action_searchFragment_to_mealsOfAreaFragment, bundle)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListOfArea(areas:List<Area>){
        if (areas != null) {
            listOfOfAreas = areas
            notifyDataSetChanged()
        } else {
            Log.e("AreaListAdapter", "setListOfArea received a null list")
        }
    }

}