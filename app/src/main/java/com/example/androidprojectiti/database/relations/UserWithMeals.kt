//package com.example.androidprojectiti.database.relations
//
//import androidx.room.Embedded
//import androidx.room.Junction
//import androidx.room.Relation
//import com.example.androidprojectiti.database.entity.User
//import com.example.androidprojectiti.dto.MealResponse.Meal
//
//data class UserWithMeals(
//    @Embedded val user: User,
//    @Relation(
//        parentColumn = "email",
//        entityColumn = "idMeal",
//        associateBy = Junction(UserFavorites::class)
//    )
//    val meals : List<Meal>
//)
