package com.example.androidprojectiti.Adapters

import android.content.SharedPreferences
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
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.CategoryResponse.Category
import com.example.androidprojectiti.dto.MealResponse.Meal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class MealAdapter(
    private val listOfOfMeals:List<Meal>,
    private val repo: UserRepo,
    private val email: String,
    private val lifecycleScope: CoroutineScope
): RecyclerView.Adapter<MealAdapter.ViewHolder>() {
    class ViewHolder(val row: View):RecyclerView.ViewHolder(row) {
        var name: TextView = row.findViewById(R.id.Name)
        var thumbnail: ImageView = row.findViewById(R.id.imageView)
        var category:TextView=row.findViewById(R.id.CategoryName)
        var favourite:ImageButton=row.findViewById(R.id.heart_button)
//        var sharedPreferences = ro
    }

//    lateinit var sharedPreferences:SharedPreferences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealAdapter.ViewHolder {
        val layout= LayoutInflater.from(parent.context).inflate(R.layout.meal_list_item,parent,false)
        return ViewHolder(layout)
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)


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
                lifecycleScope.launch {
                    repo.insertMealToFav(UserFavorites(email,listOfOfMeals[position].idMeal))
                }
            } else {
                holder.favourite.setImageResource(R.drawable.red_heart)
                lifecycleScope.launch {
                    repo.deleteMealFromFav(UserFavorites(email,listOfOfMeals[position].idMeal))
                }
            }
            isHeartRed = !isHeartRed
        }
    }

    override fun getItemCount(): Int {
        return listOfOfMeals.size
    }


}