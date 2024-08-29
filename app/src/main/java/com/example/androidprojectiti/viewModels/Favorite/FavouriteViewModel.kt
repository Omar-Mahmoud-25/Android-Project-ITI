package com.example.androidprojectiti.viewModels.Favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidprojectiti.Repositry.meal.mealRepo
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.example.androidprojectiti.dto.MealResponse.Meal
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val userRepo: UserRepo,
    private val mealRepo: mealRepo
) : ViewModel() {
    private val _MealsList = MutableLiveData<List<Meal>>()
    val MealsList: LiveData<List<Meal>> = _MealsList
    fun getFavMeals(email: String) {
       viewModelScope.launch {
           val favMealIds = userRepo.getUserFavoriteMeals(email)
           val favMeals = mutableListOf<Meal>()

           val allMeals = favMealIds.map { id ->
               async {
                   val mealResponse = mealRepo.getMealById(id)
                   if (mealResponse.isSuccessful) {
                       mealResponse.body()?.meals?.get(0)
                   } else {
                       null
                   }
               }
           }
           val meals = allMeals.awaitAll()

           favMeals.addAll(meals.filterNotNull())
           _MealsList.postValue(favMeals)
           Log.d("nada",favMeals.size.toString())
       }
   }

}
