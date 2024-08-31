package com.example.androidprojectiti.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidprojectiti.R
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.MealResponse.Meal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MealCategoryAdapter (
    private val listOfMeal : List<Meal>,
    private val userRepo : UserRepo,
    private val email: String,
    private val lifecycleScope: CoroutineScope,
    val navController: NavController
    ) : RecyclerView.Adapter<MealCategoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.meal_category_item, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {

        return listOfMeal.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = listOfMeal[position]
        holder.name.text = meal.strMeal
        holder.category.text = meal.strCategory
        holder.from.text = meal.strArea

        Glide.with(holder.image.context)
            .load(meal.strMealThumb)
            .placeholder(R.drawable.baseline_arrow_circle_down_24)
            .error(R.drawable.baseline_error_24)
            .into(holder.image)

        lifecycleScope.launch{
            val favoriteMeals = userRepo.getUserFavoriteMeals(email)
            var isFavorite = favoriteMeals.contains(meal.idMeal)


            if (isFavorite) {
                holder.heart.setImageResource(R.drawable.red_heart)
            } else {
                holder.heart.setImageResource(R.drawable.white_heart)
            }

            holder.heart.setOnClickListener {
                if (isFavorite) {
                    holder.heart.setImageResource(R.drawable.white_heart)
                    lifecycleScope.launch {
                        userRepo.deleteMealFromFav(UserFavorites(email, meal.idMeal))
                        Toast.makeText(
                            holder.itemView.context,
                            "${meal.strMeal} removed from favorites",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                } else {
                    holder.heart.setImageResource(R.drawable.red_heart)
                    lifecycleScope.launch {
                        userRepo.insertMealToFav(UserFavorites(email, meal.idMeal))
                        Toast.makeText(
                            holder.itemView.context,
                            "${meal.strMeal} added to favorites",
                            Toast.LENGTH_SHORT
                        ).show()


                    }

                }
                isFavorite = !isFavorite
            }

        }

    }


    class ViewHolder  (private val row : View) : RecyclerView.ViewHolder(row){
        var image : ImageView = row.findViewById(R.id.meal_category_item_image)
        var name : TextView = row.findViewById(R.id.meal_category_item_name)
        var category : TextView = row.findViewById(R.id.meal_category_item_category)
        var from : TextView = row.findViewById(R.id.meal_category_item_from)
        var heart : ImageButton = row.findViewById(R.id.meal_category_item_heart)
    }
}