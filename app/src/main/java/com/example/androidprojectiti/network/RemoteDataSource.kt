package com.example.androidprojectiti.network

import com.example.androidprojectiti.dto.CategoryModel
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getAllCategoriesFromRemoteDataSource(): Response<CategoryModel>

}