package com.example.androidprojectiti.viewModels
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidprojectiti.Repositry.meal.mealRepo
import com.example.androidprojectiti.dto.MealResponse.Meal
import kotlinx.coroutines.launch

class SearchViewModel(private val repo : mealRepo) : ViewModel() {

    private val _items = MutableLiveData<List<Meal>>()
    val items: LiveData<List<Meal>> = _items

//    private val _noMatches = MutableLiveData<Boolean>().apply { value = false }
//    val noMatches: LiveData<Boolean> = _noMatches
//
//    private val _searchPerformed = MutableLiveData<Boolean>().apply { value = false }
//    val searchPerformed: LiveData<Boolean> = _searchPerformed

    // Function to search and filter items based on query
    fun searchMeals(query: String) {
        val lowerCaseQuery = query.trim().lowercase()
        if (lowerCaseQuery.isNotEmpty())
            viewModelScope.launch {
                val response = repo.getMealByName(lowerCaseQuery)
                if(response.isSuccessful)
                    _items.postValue(response.body()?.meals)

                else
                    _items.postValue(emptyList())
            }
        else
            _items.postValue(emptyList())

//        val filteredItems = if (lowerCaseQuery.isBlank()) {
//            allItems
//        }
//        else {
//            allItems.filter {
//                it.name.lowercase().contains(lowerCaseQuery) ||
//                        it.description.lowercase().contains(lowerCaseQuery) ||
//                        it.category.lowercase().contains(lowerCaseQuery)
//            }
//        }

//        _items.value = filteredItems
//        _noMatches.value = filteredItems.isEmpty()
//        _searchPerformed.value = true
    }

//    fun clearSearch() {
//        _items.value = listOf()
//        _noMatches.value = false
//        _searchPerformed.value = false
//    }
}
