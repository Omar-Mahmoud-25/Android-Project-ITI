package com.example.androidprojectiti.viewModels.Favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidprojectiti.Repositry.meal.mealRepo
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.MealResponse.Meal
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val userRepo: UserRepo,
) : ViewModel() {
    private val _mealsList = MutableLiveData<List<Meal>>()
    val mealsList: LiveData<List<Meal>> = _mealsList

    fun getFavMeals(email: String) {
       viewModelScope.launch {
           val favMealIds = userRepo.getUserFavoriteMeals(email)
           _mealsList.postValue(favMealIds)
       }
   }
}
