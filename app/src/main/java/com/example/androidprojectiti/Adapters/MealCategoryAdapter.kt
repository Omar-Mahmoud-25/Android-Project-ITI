package com.example.androidprojectiti.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidprojectiti.R
import com.example.androidprojectiti.Repositry.meal.mealRepo
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.MealResponse.Meal
import com.example.androidprojectiti.fragments.MealCategoryFragmentDirections
import com.example.androidprojectiti.network.NetworkLiveData
import com.example.androidprojectiti.viewModels.MealCategory.MealCategoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response

class MealCategoryAdapter (
    private var listOfMeal : List<Meal>,
    private val mealRepo: mealRepo,
    private val userRepo : UserRepo,
    private val email: String,
    private val lifecycleScope: CoroutineScope,
    private val cat : String,
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
        holder.name.text = listOfMeal[position].strMeal
        holder.category.text = cat

        Glide.with(holder.image.context)
            .load(listOfMeal[position].strMealThumb)
            .placeholder(R.drawable.baseline_arrow_circle_down_24)
            .error(R.drawable.baseline_error_24)
            .into(holder.image)


        holder.itemView.setOnClickListener {
            var meal = listOfMeal[position]
            lifecycleScope.launch {
                val response = mealRepo.getMealById(listOfMeal[position].idMeal)
                if (response.isSuccessful){
                    meal = response.body()?.meals?.get(0) ?: listOfMeal[position]
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


            if (isFavorite) {
                holder.heart.setImageResource(R.drawable.red_heart)
            } else {
                holder.heart.setImageResource(R.drawable.white_heart)
            }

            holder.heart.setOnClickListener {
                lifecycleScope.launch {
                    if (isFavorite) {
                        holder.heart.setImageResource(R.drawable.white_heart)
                        lifecycleScope.launch {
                            userRepo.deleteMealFromFav(UserFavorites(email,meal.body()?.meals?.get(0) ?: listOfMeal[position]))
                            Toast.makeText(
                                holder.itemView.context,
                                "${listOfMeal[position].strMeal} removed from favorites",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    } else {
                        holder.heart.setImageResource(R.drawable.red_heart)
                        lifecycleScope.launch {
                            userRepo.insertMealToFav(UserFavorites(email, meal.body()?.meals?.get(0) ?: listOfMeal[position]))
                            Toast.makeText(
                                holder.itemView.context,
                                "${listOfMeal[position].strMeal} added to favorites",
                                Toast.LENGTH_SHORT
                            ).show()


                        }

                    }
                    isFavorite = !isFavorite
                }



            }



        }

    }


    class ViewHolder  (private val row : View) : RecyclerView.ViewHolder(row){
        var image : ImageView = row.findViewById(R.id.meal_category_item_image)
        var name : TextView = row.findViewById(R.id.meal_category_item_name)
        var category : TextView = row.findViewById(R.id.meal_category_item_category)
        var heart : ImageButton = row.findViewById(R.id.meal_category_item_heart)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setListOfMeal(meal : List<Meal>){
        listOfMeal = meal
        notifyDataSetChanged()
    }
}