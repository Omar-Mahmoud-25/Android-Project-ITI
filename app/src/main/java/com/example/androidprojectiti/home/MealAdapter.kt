package com.example.androidprojectiti.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidprojectiti.R
import com.example.androidprojectiti.repositories.user.UserRepo
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.MealResponse.Meal
import com.example.androidprojectiti.onClickFavorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MealAdapter(
    private var listOfOfMeals: List<Meal>,
    private val userRepo: UserRepo,
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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
            val favoriteMeals = userRepo.getUserFavoriteMeals(email)
            isFavorite = favoriteMeals.contains(meal)

            if (isFavorite)
                holder.favourite.setImageResource(R.drawable.red_heart)
            else
                holder.favourite.setImageResource(R.drawable.white_heart)

        }

        holder.favourite.setOnClickListener {
            onClickFavorite(
                isFavorite = isFavorite,
                repo = userRepo,
                email = email,
                meal = listOfOfMeals[position],
                context = holder.itemView.context,
                heart = holder.favourite,
                lifecycleScope = lifecycleScope
            )
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
    fun updateListOfMeal (meal : List<Meal>){
        listOfOfMeals = meal
        notifyDataSetChanged()
    }
}
