package com.example.androidprojectiti.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {
    private val gson = GsonBuilder().serializeNulls().create()
    private val retrofit= Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    val service:ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

}