package com.example.androidprojectiti.search

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
import com.example.androidprojectiti.repositories.user.UserRepo
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.MealResponse.Meal
import com.example.androidprojectiti.onClickFavorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class MealSearchAdapter(
    private var meals: List<Meal>,
    private val userRepo : UserRepo,
    private val email: String,
    private val lifecycleScope: CoroutineScope,
    private val navController: NavController
) : RecyclerView.Adapter<MealSearchAdapter.ViewHolder>() {

    // Create a new ViewHolder instance
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meal_search_layout, parent, false)
        return ViewHolder(view)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = meals[position]
        holder.nameTextView.text = meal.strMeal
        holder.areaTextView.text = "From: ${meal.strArea} Meal"
        holder.categoryTextView.text = meal.strCategory

        // Load image into the ImageView using Glide
        Glide.with(holder.itemView.context)
            .load(meal.strMealThumb)
            .placeholder(R.drawable.ic_launcher_foreground) // Placeholder image
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            val action =
                SearchFragmentDirections.actionSearchFragmentToRecipeDetailFragment(meals[position])
            navController.navigate(action)
        }

        lifecycleScope.launch {
            try {
                val favoriteMeals = userRepo.getUserFavoriteMeals(email)
                var isFavorite = favoriteMeals.contains(meals[position])

                if (isFavorite)
                    holder.imageButton.setImageResource(R.drawable.red_heart)
                else
                    holder.imageButton.setImageResource(R.drawable.white_heart)

                holder.imageButton.setOnClickListener {
                    onClickFavorite(
                        isFavorite = isFavorite,
                        repo = userRepo,
                        email = email,
                        meal = meals[position],
                        context = holder.itemView.context,
                        heart = holder.imageButton,
                        lifecycleScope = lifecycleScope
                    )
                    isFavorite = !isFavorite
                }
            }
            catch (exception: Exception) {
                Toast.makeText(holder.itemView.context, "Sorry, try again", Toast.LENGTH_SHORT)
                    .show()
            }
        }
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
        val imageButton : ImageButton = itemView.findViewById(R.id.item_favorite)
    }
}
