package com.example.androidprojectiti.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidprojectiti.Repositry.meal.mealRepo
import com.example.androidprojectiti.dto.MealResponse.Meal
import kotlinx.coroutines.launch

class SearchViewModel(private val repo : mealRepo) : ViewModel() {

    // Sample data
    private val allItems = listOf<Meal>(
//        Item("Item 1", "Description 1", "Category 1", R.drawable.ic_launcher_foreground),
//        Item("Item 2", "Description 2", "Category 2", R.drawable.ic_launcher_foreground),
//        Item("Item 3", "Description 3", "Category 3", R.drawable.ic_launcher_foreground),
//        Item("Item 4", "Description 4", "Category 4", R.drawable.ic_launcher_foreground),
//        Item("Item 5", "Description 5", "Category 5", R.drawable.ic_launcher_foreground),
//        Item("Item 6", "Description 6", "Category 6", R.drawable.ic_launcher_foreground),
//        Item("Item 7", "Description 7", "Category 7", R.drawable.ic_launcher_foreground),
//        Item("Item 8", "Description 8", "Category 8", R.drawable.ic_launcher_foreground),
//        Item("Item 9", "Description 9", "Category 9", R.drawable.ic_launcher_foreground),
//        Item("Item 10", "Description 10", "Category 10", R.drawable.ic_launcher_foreground)
        // Add more items as needed
    )

    private val _items = MutableLiveData<List<Meal>>().apply { value = allItems }
    val items: LiveData<List<Meal>> = _items

    private val _noMatches = MutableLiveData<Boolean>().apply { value = false }
    val noMatches: LiveData<Boolean> = _noMatches

    private val _searchPerformed = MutableLiveData<Boolean>().apply { value = false }
    val searchPerformed: LiveData<Boolean> = _searchPerformed

    // Function to search and filter items based on query
    fun searchMeals(query: String) {
        val lowerCaseQuery = query.trim().lowercase()
          viewModelScope.launch {
              val response =repo.getMealByName(lowerCaseQuery)
           if(response.isSuccessful)
           {
               _items.postValue(response.body()?.meals)
           }

          }

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

    fun clearSearch() {
        _items.value = allItems
        _noMatches.value = false
        _searchPerformed.value = false
    }
}
