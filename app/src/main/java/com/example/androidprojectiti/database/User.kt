package com.example.androidprojectiti.database

import androidx.room.Entity
import androidx.room.PrimaryKey


data class User(
    val firstName: String,
    val lastName : String,
    val age: Int,
    val email: String,
    val password: String,
)