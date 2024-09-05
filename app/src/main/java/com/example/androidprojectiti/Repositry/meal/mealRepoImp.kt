package com.example.androidprojectiti.Repositry.meal

import com.example.androidprojectiti.dto.CategoryResponse.CategoryModel
import com.example.androidprojectiti.dto.MealResponse.MealModel
import com.example.androidprojectiti.network.RemoteDataSource
import retrofit2.Response

class mealRepoImp (
    private var remoteDataSource: RemoteDataSource

    ) : mealRepo {
    override suspend fun getAllMeals(letter: Char): Response<MealModel> {
        return remoteDataSource.getMealsFromRemoteDataSource(letter)
    }

    override suspend fun getRandomMeal(): Response<MealModel> {
        return remoteDataSource.getRandomMealFromRemoteDataSource()
    }

    override suspend fun getMealById(id: String): Response<MealModel> {
        return remoteDataSource.getMealByIdFromRemoteDataSource(id)
    }

    override suspend fun getMealByName(name: String): Response<MealModel> {
        return remoteDataSource.getMealBNameFromRemoteDataSource(name)
    }

    override suspend fun getMealsByCategory(category: String): Response<MealModel> {
        return remoteDataSource.getMealsByCategoryFromRemoteDataSource(category)
    }

}
