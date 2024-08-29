package com.example.androidprojectiti.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidprojectiti.Adapters.MealAdapter.ViewHolder
import com.example.androidprojectiti.R
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.MealResponse.Meal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class favoriteAdapter(
    private val listOfOfFavorite:MutableList<Meal>,
    private val repo: UserRepo,
    private val email: String,
    private val lifecycleScope: CoroutineScope,
): RecyclerView.Adapter<favoriteAdapter.ViewHolder>() {
    class ViewHolder(val row: View):RecyclerView.ViewHolder(row) {
        var name: TextView = row.findViewById(R.id.name)
        var thumbnail: ImageView = row.findViewById(R.id.imageView2)
        var category: TextView =row.findViewById(R.id.category)
        var tags:TextView=row.findViewById(R.id.tags)
        var from:TextView=row.findViewById(R.id.from)
    //        var sharedPreferences = ro
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout= LayoutInflater.from(parent.context).inflate(R.layout.favorite_item_list,parent,false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return listOfOfFavorite.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favMeal = listOfOfFavorite[position]
        holder.name.text=favMeal.strMeal
        holder.category.text = favMeal.strCategory
        holder.from.text=favMeal.strArea
        holder.tags.text=favMeal.strTags
        Glide.with(holder.thumbnail.context)
            .load(favMeal.strMealThumb)
            .placeholder(R.drawable.baseline_arrow_circle_down_24)
            .error(R.drawable.baseline_error_24)
            .into(holder.thumbnail)

    }

    fun removeAt(position: Int,context:Context) {
        val favMeal = listOfOfFavorite[position]
        listOfOfFavorite.removeAt(position)

        lifecycleScope.launch {
            repo.deleteMealFromFav(UserFavorites(email, favMeal.idMeal))
            Toast.makeText(context, "${favMeal.strMeal} removed from favorites", Toast.LENGTH_SHORT).show()
        }
        notifyItemRemoved(position)
    }

}