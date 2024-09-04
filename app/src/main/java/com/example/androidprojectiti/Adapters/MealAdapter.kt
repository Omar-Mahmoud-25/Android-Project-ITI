package com.example.androidprojectiti.Adapters

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidprojectiti.Adapters.CategoryAdapter.ViewHolder
import com.example.androidprojectiti.R
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.CategoryResponse.Category
import com.example.androidprojectiti.dto.MealResponse.Meal
import com.example.androidprojectiti.fragments.HomeFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class MealAdapter(
    private var listOfOfMeals: List<Meal>,
    private val repo: UserRepo,
    private val email: String,
    private val lifecycleScope: CoroutineScope,
    val navController: NavController
) : RecyclerView.Adapter<MealAdapter.ViewHolder>() {
    class ViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        var name: TextView = row.findViewById(R.id.Name)
        var thumbnail: ImageView = row.findViewById(R.id.imageView)
        var category: TextView = row.findViewById(R.id.Category_name)
        var area:TextView=row.findViewById(R.id.Area)
        var favourite: ImageButton = row.findViewById(R.id.heart_button)
//        var sharedPreferences = ro
    }

//    lateinit var sharedPreferences:SharedPreferences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealAdapter.ViewHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.meal_list_item, parent, false)
        return ViewHolder(layout)
    }


    private val coroutineScope = CoroutineScope(Dispatchers.IO)


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = listOfOfMeals[position]
        holder.name.text = meal.strMeal
        holder.category.text = meal.strCategory
        holder.area.text=meal.strArea
        Glide.with(holder.thumbnail.context)
            .load(listOfOfMeals[position].strMealThumb)
            .placeholder(R.drawable.baseline_arrow_circle_down_24)
            .error(R.drawable.baseline_error_24)
            .into(holder.thumbnail)

        var isFavorite = false

        lifecycleScope.launch {
            val favoriteMeals = repo.getUserFavoriteMeals(email)
            isFavorite = favoriteMeals.contains(meal)

            if (isFavorite) {
                holder.favourite.setImageResource(R.drawable.red_heart)
            } else {
                holder.favourite.setImageResource(R.drawable.white_heart)
            }


        }

        holder.favourite.setOnClickListener {
            if (isFavorite) {
                holder.favourite.setImageResource(R.drawable.white_heart)
                lifecycleScope.launch {
                    repo.deleteMealFromFav(UserFavorites(email, meal))
                    Toast.makeText(
                        holder.itemView.context,
                        "${listOfOfMeals[position].strMeal} removed from favorites",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            } else {
                holder.favourite.setImageResource(R.drawable.red_heart)
                lifecycleScope.launch {
                    repo.insertMealToFav(UserFavorites(email, meal))
                    Toast.makeText(
                        holder.itemView.context,
                        "${listOfOfMeals[position].strMeal} added to favorites",
                        Toast.LENGTH_SHORT
                    ).show()


                }

            }
            isFavorite = !isFavorite
        }

        holder.itemView.setOnClickListener {
            val item = listOfOfMeals[position]
            item.putDefaults()
            val action = HomeFragmentDirections.actionHomeFragmentToRecipeDetailFragment(item)
            navController.navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return listOfOfMeals.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListOfMeal (meal : List<Meal>){
        listOfOfMeals = meal
        notifyDataSetChanged()
    }
    fun updateMealDetail(meal: Meal) {
        val updatedList = listOfOfMeals.map {
            if (it.idMeal == meal.idMeal) meal else it
        }
        notifyDataSetChanged()
    }
}
