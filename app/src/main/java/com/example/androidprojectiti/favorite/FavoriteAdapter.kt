package com.example.androidprojectiti.favorite

import android.content.Context
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class favoriteAdapter(
    private var listOfFavorite: MutableList<Meal>,
    private val repo: UserRepo,
    private val email: String,
    private val lifecycleScope: CoroutineScope,
    private val context: Context,
    private val viewModel: FavouriteViewModel,
    private val showConfirmationDialog: (onConfirm: () -> Unit) -> Unit,
    private val navController: NavController
) : RecyclerView.Adapter<favoriteAdapter.ViewHolder>() {

    class ViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        var name: TextView = row.findViewById(R.id.name)
        var thumbnail: ImageView = row.findViewById(R.id.imageView2)
        var category: TextView = row.findViewById(R.id.category)
        var from: TextView = row.findViewById(R.id.from)
        var deleteButton:ImageButton=row.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.favorite_item_list, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return listOfFavorite.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favMeal = listOfFavorite[position]
        holder.name.text = favMeal.strMeal
        holder.category.text = favMeal.strCategory
        holder.from.text = favMeal.strArea

        Glide.with(holder.thumbnail.context)
            .load(favMeal.strMealThumb)
            .placeholder(R.drawable.baseline_arrow_circle_down_24)
            .error(R.drawable.baseline_error_24)
            .into(holder.thumbnail)
        holder.deleteButton.setOnClickListener {
             showConfirmationDialog {
                 removeAt(position)
             }
        }
        holder.itemView.setOnClickListener {
            val item = listOfFavorite[position]
            item.putDefaults()
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToRecipeDetailFragment2(item)
            navController.navigate(action)
        }
    }
    fun updateMeals(newMeals: MutableList<Meal>) {
        listOfFavorite = newMeals
        notifyDataSetChanged()  // Notify the adapter that the data has changed
    }
    fun removeAt(position: Int) {
        val favMeal = listOfFavorite[position]
        listOfFavorite.removeAt(position)
        notifyItemRemoved(position)
        lifecycleScope.launch {
            repo.deleteMealFromFav(UserFavorites(email, favMeal))
            Toast.makeText(context, "${favMeal.strMeal} removed from favorites", Toast.LENGTH_SHORT).show()
            viewModel.getFavMeals(email)
        }
    }

}
