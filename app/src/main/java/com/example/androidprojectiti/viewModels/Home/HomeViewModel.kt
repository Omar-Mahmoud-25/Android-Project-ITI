package com.example.androidprojectiti.viewModels.Home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidprojectiti.R
import com.example.androidprojectiti.Repositry.category.categoryRepo
import com.example.androidprojectiti.Repositry.meal.mealRepo
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.example.androidprojectiti.dto.CategoryResponse.Category
import com.example.androidprojectiti.dto.MealResponse.Meal
import kotlinx.coroutines.launch

class HomeViewModel(
    private val categoryRepo: categoryRepo,
    private val mealRepo: mealRepo,
    private val userRepo: UserRepo
): ViewModel()  {
    private val _CategoryList= MutableLiveData<List<Category>>()
    val CategoryList: LiveData<List<Category>> = _CategoryList
    private val _RandomMeal=MutableLiveData<List<Meal>>()
    val RandomMeal:LiveData<List<Meal>> =_RandomMeal
    private val _isRemoteSourceError= MutableLiveData<String>()
    val isRemoteSourceError: LiveData<String> = _isRemoteSourceError
    private var _userName = MutableLiveData<String>()
    val userName : LiveData<String> = _userName

    private val _MealsList= MutableLiveData<List<Meal>>()
    val MealsList: LiveData<List<Meal>> = _MealsList

    fun getUserName(email: String){
        viewModelScope.launch {
            val user = userRepo.getUser(email)[0]
            _userName.value = "${user.firstName} ${user.lastName}"
        }
    }

    fun getCategories(){
        viewModelScope.launch {
            val categoryResponse=categoryRepo.getAllCategories()
            if (categoryResponse.isSuccessful){
                _CategoryList.postValue(categoryResponse.body()?.categories)
            }else{

            }
        }
    }

    fun getMeals(){
        val chars = ('a'..'z')
        viewModelScope.launch {
            val mealResponse=mealRepo.getAllMeals('s')
            if (mealResponse.isSuccessful){
                _MealsList.postValue(mealResponse.body()?.meals)
            }
            else{

            }
        }
    }

    fun getRandomMeal(){
        viewModelScope.launch {
            val mealResponse=mealRepo.getRandomMeal()
            if (mealResponse.isSuccessful){
                _RandomMeal.postValue(mealResponse.body()?.meals)
                Log.d("nada","success")
            }
            else{
                Log.d("nada","failed")
            }
        }
    }
}