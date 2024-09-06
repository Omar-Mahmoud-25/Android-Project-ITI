package com.example.androidprojectiti.mealCategory

import android.annotation.SuppressLint
import android.util.Log
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
import com.example.androidprojectiti.repositories.meal.mealRepo
import com.example.androidprojectiti.repositories.user.UserRepo
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.MealResponse.Meal
import com.example.androidprojectiti.onClickFavorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MealCategoryAdapter (
    private var listOfMeal : List<Meal>,
    private val mealRepo: mealRepo,
    private val userRepo : UserRepo,
    private val email: String,
    private val lifecycleScope: CoroutineScope,
    val navController: NavController
    ) : RecyclerView.Adapter<MealCategoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.meal_category_item, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int = listOfMeal.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = listOfMeal[position].strMeal

        Glide.with(holder.image.context)
            .load(listOfMeal[position].strMealThumb)
            .placeholder(R.drawable.baseline_arrow_circle_down_24)
            .error(R.drawable.baseline_error_24)
            .into(holder.image)


        holder.itemView.setOnClickListener {
            lifecycleScope.launch {
                val response = mealRepo.getMealById(listOfMeal[position].idMeal)
                if (response.isSuccessful){
                    val meal = response.body()?.meals?.get(0) ?: listOfMeal[position]
                    meal.putDefaults()
                    val action = MealCategoryFragmentDirections.actionMealCategoryFragmentToRecipeDetailFragment(meal)
                    Log.d("maro", meal.strMeal.toString())
                    navController.navigate(action)
                }
            }

        }

        lifecycleScope.launch{
            val meal = mealRepo.getMealById(listOfMeal[position].idMeal)
            val favoriteMeals = userRepo.getUserFavoriteMeals(email)
            var isFavorite = favoriteMeals.contains(meal.body()?.meals?.get(0) ?: listOfMeal[position])


            if (isFavorite)
                holder.heart.setImageResource(R.drawable.red_heart)
            else
                holder.heart.setImageResource(R.drawable.white_heart)

            holder.heart.setOnClickListener {
                lifecycleScope.launch {
                    onClickFavorite(
                        isFavorite = isFavorite,
                        repo = userRepo,
                        email = email,
                        meal = meal.body()?.meals?.get(0) ?: listOfMeal[position],
                        context = holder.itemView.context,
                        heart = holder.heart,
                        lifecycleScope = lifecycleScope
                    )
                    isFavorite = !isFavorite
                }
            }
        }

    }


    class ViewHolder  (private val row : View) : RecyclerView.ViewHolder(row){
        var image : ImageView = row.findViewById(R.id.meal_category_item_image)
        var name : TextView = row.findViewById(R.id.meal_category_item_name)
        var heart : ImageButton = row.findViewById(R.id.meal_category_item_heart)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setListOfMeal(meal : List<Meal>){
        listOfMeal = meal
        notifyDataSetChanged()
    }
}