package com.example.androidprojectiti.mealCategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidprojectiti.repositories.meal.mealRepo
import com.example.androidprojectiti.repositories.user.UserRepo
import com.example.androidprojectiti.dto.MealResponse.Meal
import kotlinx.coroutines.launch

class MealCategoryViewModel(
    private val mealRepo: mealRepo,
    private val userRepo: UserRepo
) : ViewModel(){

    private val _mealsList= MutableLiveData<List<Meal>>()
    val mealsList: LiveData<List<Meal>> = _mealsList

    private val _mealById = MutableLiveData<Meal>()
    val mealById = _mealById


    fun getMealsByCategory(category: String){
        viewModelScope.launch{
            val response =mealRepo.getMealsByCategory(category)

            if(response.isSuccessful){
                _mealsList.postValue(response.body()?.meals)
            }
        }
    }

    fun getMealById(id : String){
        viewModelScope.launch {
            val response = mealRepo.getMealById(id)

            if (response.isSuccessful){
                _mealById.postValue(response.body()?.meals?.get(0))
            }
        }
    }


}