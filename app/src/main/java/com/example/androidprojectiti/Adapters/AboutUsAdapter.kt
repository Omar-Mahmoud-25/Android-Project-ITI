package com.example.androidprojectiti.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidprojectiti.Contributor
import com.example.androidprojectiti.R

class AboutUsAdapter(
    private val contributors : List<Contributor>
): RecyclerView.Adapter<AboutUsAdapter.ViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout= LayoutInflater.from(parent.context).inflate(R.layout.about_us_item, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contributor = contributors[position]
        holder.name.text = contributor.name
        holder.email.text = contributor.email

        holder.image.setImageResource(contributor.image)
    }

    override fun getItemCount(): Int {
        return contributors.size
    }




    class ViewHolder(val row: View):RecyclerView.ViewHolder(row){
        var name : TextView = row.findViewById(R.id.name)
        var email : TextView = row.findViewById(R.id.email)
        var image : ImageView = row.findViewById(R.id.about_us_image)
    }
}