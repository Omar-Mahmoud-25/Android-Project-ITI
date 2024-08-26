package com.example.androidprojectiti.viewModels.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidprojectiti.category.categoryRepo
import com.example.androidprojectiti.dto.Category
import kotlinx.coroutines.launch

class HomeViewModel(
    private val categoryRepo:categoryRepo
): ViewModel()  {
    private val _CategoryList= MutableLiveData<List<Category>>()
    val CategoryList: LiveData<List<Category>> = _CategoryList
    private val _isRemoteSourceError= MutableLiveData<String>()
    val isRemoteSourceError: LiveData<String> = _isRemoteSourceError

    fun getCategories(){

        viewModelScope.launch {
            val response=categoryRepo.getAllCategories()
            if (response.isSuccessful){
                _CategoryList.postValue(response.body()?.categories)
            }else{

            }
        }

    }
}