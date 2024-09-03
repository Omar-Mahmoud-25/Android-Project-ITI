package com.example.androidprojectiti.viewModels.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidprojectiti.Repositry.Area.AreaRepo
import com.example.androidprojectiti.Repositry.meal.mealRepo
import com.example.androidprojectiti.dto.AreaResponse.Area
import com.example.androidprojectiti.dto.MealResponse.Meal
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repo: mealRepo,
    private val areaRepo:AreaRepo
) : ViewModel() {

    private val _items = MutableLiveData<List<Meal>>().apply { value = emptyList() }
    val items: LiveData<List<Meal>> = _items

    private val _noMatches = MutableLiveData<Boolean>().apply { value = false }
    val noMatches: LiveData<Boolean> = _noMatches

    private val _searchPerformed = MutableLiveData<Boolean>().apply { value = false }
    val searchPerformed: LiveData<Boolean> = _searchPerformed

    private val _Areas = MutableLiveData<List<Area>>().apply { value = emptyList() }
    val Areas: LiveData<List<Area>> = _Areas
    // Function to search and filter items based on query
    fun searchMeals(query: String) {
        val lowerCaseQuery = query.trim().lowercase()
        viewModelScope.launch {
            if (lowerCaseQuery.isEmpty()) {
                clearSearch() // Clear results if query is empty
            } else {
                val response = repo.getMealByName(lowerCaseQuery)
                if (response.isSuccessful) {
                    val meals = response.body()?.meals ?: emptyList()
                    _items.postValue(meals)
                    _noMatches.postValue(meals.isEmpty())
                    _searchPerformed.postValue(true)
                } else {
                    _items.postValue(emptyList())
                    _noMatches.postValue(true)
                    _searchPerformed.postValue(true)
                }
            }
        }
    }
    fun getAreas(list:String){
        viewModelScope.launch{
            val response =areaRepo.getaLLAreas(list)

            if(response.isSuccessful){
                _Areas.postValue(response.body()?.meals)
            }
            else{

            }
        }
    }

    fun clearSearch() {
        _items.value = emptyList()  // Clear items
        _noMatches.value = true     // Indicate no matches
        _searchPerformed.value = false
    }
}
