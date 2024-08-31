package com.example.androidprojectiti.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidprojectiti.R
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.MealResponse.Meal
import com.example.androidprojectiti.fragments.FavoriteFragment
import com.example.androidprojectiti.fragments.FavoriteFragmentDirections
import com.example.androidprojectiti.fragments.HomeFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class favoriteAdapter(
    private val listOfFavorite: MutableList<Meal>,
    private val repo: UserRepo,
    private val email: String,
    private val lifecycleScope: CoroutineScope,
    private val context: Context,
    private val showConfirmationDialog: (onConfirm: () -> Unit) -> Unit,
    val navController: NavController
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
              removeAt(position, context)
          }
      }
        holder.itemView.setOnClickListener {
            val item = listOfFavorite[position]
            item.putDefaults()
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToRecipeDetailFragment2(item)
            navController.navigate(action)
        }
    }

    fun removeAt(position: Int,context:Context) {
        val favMeal = listOfFavorite[position]
        listOfFavorite.removeAt(position)

        lifecycleScope.launch {
            repo.deleteMealFromFav(UserFavorites(email, favMeal.idMeal))
            Toast.makeText(context, "${favMeal.strMeal} removed from favorites", Toast.LENGTH_SHORT).show()
        }
        notifyItemRemoved(position)
    }

}
