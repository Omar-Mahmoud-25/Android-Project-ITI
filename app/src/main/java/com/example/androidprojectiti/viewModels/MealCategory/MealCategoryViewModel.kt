package com.example.androidprojectiti.viewModels.MealCategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidprojectiti.Repositry.meal.mealRepo
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.example.androidprojectiti.dto.MealResponse.Meal
import kotlinx.coroutines.launch

class MealCategoryViewModel(
    private val mealRepo: mealRepo,
    private val userRepo: UserRepo
) : ViewModel(){

    private val _mealsList= MutableLiveData<List<Meal>>()
    val mealsList: LiveData<List<Meal>> = _mealsList


    fun getMealsByCategory(category: String){
        viewModelScope.launch{
            val response =mealRepo.getMealsByCategory(category)

            if(response.isSuccessful){
                _mealsList.postValue(response.body()?.meals)
            }
        }
    }


}