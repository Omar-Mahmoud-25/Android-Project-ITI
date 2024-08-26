package com.example.androidprojectiti.network

import com.example.androidprojectiti.dto.CategoryModel
import retrofit2.Response

object ApiClient :RemoteDataSource{
    override suspend fun getAllCategoriesFromRemoteDataSource(): Response<CategoryModel> {
        return Api.service.getAllCategories()
    }
}