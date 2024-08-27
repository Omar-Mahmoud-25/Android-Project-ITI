package com.example.androidprojectiti.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val userId : String,
    val firstName: String,
    val lastName : String,
    val age: Int,
    val email: String,
    val password: String,
)