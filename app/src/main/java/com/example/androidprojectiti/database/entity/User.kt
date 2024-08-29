package com.example.androidprojectiti.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    val firstName: String,
    val lastName : String,
    val age: Int,
    @PrimaryKey
    val email: String,
    val password: String,
)