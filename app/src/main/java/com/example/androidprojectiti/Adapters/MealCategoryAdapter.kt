package com.example.androidprojectiti.Adapters

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
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.MealResponse.Meal
import com.example.androidprojectiti.fragments.MealCategoryFragmentDirections
import com.example.androidprojectiti.network.NetworkLiveData
import com.example.androidprojectiti.viewModels.MealCategory.MealCategoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MealCategoryAdapter (
    private val listOfMeal : List<Meal>,
    private val userRepo : UserRepo,
    private val email: String,
    private val lifecycleScope: CoroutineScope,
    private val cat : String,
    private val network : NetworkLiveData,
    private val requireActivity : FragmentActivity,
    private val context : Context,
    private val retrofit : MealCategoryViewModel,
    private val lifecycleOwner: LifecycleOwner,
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

        network.observe(requireActivity) {
            if (it) {
                retrofit.getMealById(listOfMeal[position].idMeal)
            } else {
                Toast.makeText(context, "No Internet", Toast.LENGTH_LONG).show()
            }
        }

        holder.name.text = listOfMeal[position].strMeal
        holder.category.text = cat

        Glide.with(holder.image.context)
            .load(listOfMeal[position].strMealThumb)
            .placeholder(R.drawable.baseline_arrow_circle_down_24)
            .error(R.drawable.baseline_error_24)
            .into(holder.image)

        var meal  = listOfMeal[position]
        retrofit.mealById.observe(lifecycleOwner){
            meal = it
        }

        holder.itemView.setOnClickListener {
            meal.putDefaults()
            val action = MealCategoryFragmentDirections.actionMealCategoryFragmentToRecipeDetailFragment(meal)
            navController.navigate(action)
        }

        lifecycleScope.launch{
            val favoriteMeals = userRepo.getUserFavoriteMeals(email)
            var isFavorite = favoriteMeals.contains(listOfMeal[position])


            if (isFavorite) {
                holder.heart.setImageResource(R.drawable.red_heart)
            } else {
                holder.heart.setImageResource(R.drawable.white_heart)
            }

            holder.heart.setOnClickListener {
                if (isFavorite) {
                    holder.heart.setImageResource(R.drawable.white_heart)
                    lifecycleScope.launch {
                        userRepo.deleteMealFromFav(UserFavorites(email, listOfMeal[position]))
                        Toast.makeText(
                            holder.itemView.context,
                            "${listOfMeal[position].strMeal} removed from favorites",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                } else {
                    holder.heart.setImageResource(R.drawable.red_heart)
                    lifecycleScope.launch {
                        userRepo.insertMealToFav(UserFavorites(email, listOfMeal[position]))
                        Toast.makeText(
                            holder.itemView.context,
                            "${listOfMeal[position].strMeal} added to favorites",
                            Toast.LENGTH_SHORT
                        ).show()


                    }

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
}