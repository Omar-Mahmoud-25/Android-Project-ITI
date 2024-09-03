package com.example.myapplicationrecyclarview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidprojectiti.R
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.MealResponse.Meal
import com.example.androidprojectiti.fragments.FavoriteFragmentDirections
import com.example.androidprojectiti.fragments.SearchFragmentDirections
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
        try{
            val meal = meals[position]
            holder.nameTextView.text = meal.strMeal
            holder.areaTextView.text = "${meal.strArea} Meal"
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
                val favoriteMeals = userRepo.getUserFavoriteMeals(email)
                val isFavorite = favoriteMeals.contains(meals[position])


                if (isFavorite) {
                    holder.imageButton.setImageResource(R.drawable.red_heart)
                } else {
                    holder.imageButton.setImageResource(R.drawable.white_heart)
                }

                holder.imageButton.setOnClickListener {
                    if (isFavorite) {
                        holder.imageButton.setImageResource(R.drawable.white_heart)
                        lifecycleScope.launch {
                            userRepo.deleteMealFromFav(UserFavorites(email, meals[position]))
                            Toast.makeText(
                                holder.itemView.context,
                                "${meals[position].strMeal} removed from favorites",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    } else {
                        holder.imageButton.setImageResource(R.drawable.red_heart)
                        lifecycleScope.launch {
                            userRepo.insertMealToFav(UserFavorites(email, meals[position]))
                            Toast.makeText(
                                holder.itemView.context,
                                "${meals[position].strMeal} added to favorites",
                                Toast.LENGTH_SHORT
                            ).show()


                        }

                    }
                }
            }
        }
        catch (exception: IndexOutOfBoundsException){
            Toast.makeText(holder.itemView.context,"Sorry, try again",Toast.LENGTH_SHORT).show()
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
        val imageButton : ImageView = itemView.findViewById(R.id.item_favorite)
    }
}
