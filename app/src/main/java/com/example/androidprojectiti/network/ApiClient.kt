package com.example.androidprojectiti.network

import com.example.androidprojectiti.dto.CategoryResponse.CategoryModel
import com.example.androidprojectiti.dto.MealResponse.MealModel
import retrofit2.Response

object ApiClient :RemoteDataSource{
    override suspend fun getAllCategoriesFromRemoteDataSource(): Response<CategoryModel> {
        return Api.service.getAllCategories()
    }

    override suspend fun getMealsFromRemoteDataSource(letter:Char): Response<MealModel> {
        return Api.service.searchMeals(letter)
    }

    override suspend fun getRandomMealFromRemoteDataSource(): Response<MealModel> {
        return Api.service.getRandomMeal()
    }

    override suspend fun getMealByIdFromRemoteDataSource(id: String): Response<MealModel> {
        return Api.service.getMealByID(id)
    }

    override suspend fun getMealsByCategoryFromRemoteDataSource(category: String): Response<MealModel> {
        return Api.service.getMealsByCategory(category)
    }

    override suspend fun getMealBNameFromRemoteDataSource(name: String): Response<MealModel> {
        return Api.service.getMealByName(name)
    }

}