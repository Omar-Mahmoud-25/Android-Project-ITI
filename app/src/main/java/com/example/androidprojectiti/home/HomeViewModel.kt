package com.example.androidprojectiti.home

import android.content.Context
import android.util.Log
import android.widget.ImageButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.androidprojectiti.R
import com.example.androidprojectiti.repositories.category.categoryRepo
import com.example.androidprojectiti.repositories.meal.mealRepo
import com.example.androidprojectiti.repositories.user.UserRepo
import com.example.androidprojectiti.dto.CategoryResponse.Category
import com.example.androidprojectiti.dto.MealResponse.Meal
import com.example.androidprojectiti.onClickFavorite
import kotlinx.coroutines.launch

class HomeViewModel(
    private val categoryRepo: categoryRepo,
    private val mealRepo: mealRepo,
    private val userRepo: UserRepo,
): ViewModel()  {
    private val _categoryList= MutableLiveData<List<Category>>()
    val categoryList: LiveData<List<Category>> = _categoryList
    private val _randomMeal=MutableLiveData<List<Meal>>()
    val randomMeal:LiveData<List<Meal>> =_randomMeal
    private val _isRemoteSourceError= MutableLiveData<String>()
    val isRemoteSourceError: LiveData<String> = _isRemoteSourceError
    private var _userName = MutableLiveData<String>()
    val userName : LiveData<String> = _userName

    private val _mealsList= MutableLiveData<List<Meal>>()
    val mealsList: LiveData<List<Meal>> = _mealsList

    fun getUserName(email: String){
        viewModelScope.launch {
            val user = userRepo.getUser(email)
            if (user.isNotEmpty())
                _userName.value = user[0].firstName
            else
                _userName.value = "guest"
        }
    }

    fun getCategories(){
        viewModelScope.launch {
            val categoryResponse=categoryRepo.getAllCategories()
            if (categoryResponse.isSuccessful)
                _categoryList.postValue(categoryResponse.body()?.categories)
        }
    }

    fun getMeals(){
        val chars = ('a'..'z')
        viewModelScope.launch {
            val mealResponse=mealRepo.getAllMeals(chars.random())
            if (mealResponse.isSuccessful)
                _mealsList.postValue(mealResponse.body()?.meals)
        }
    }

    fun getRandomMeal(){
        viewModelScope.launch {
            val mealResponse=mealRepo.getRandomMeal()
            if (mealResponse.isSuccessful)
                _randomMeal.postValue(mealResponse.body()?.meals)
        }
    }

}