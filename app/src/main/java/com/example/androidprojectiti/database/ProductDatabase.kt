package com.example.androidprojectiti.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.androidprojectiti.database.dao.UserDao
import com.example.androidprojectiti.database.entity.User
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.MealResponse.Meal

@Database(entities = [User::class, UserFavorites::class], version = 2)
@TypeConverters(Converters::class)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun UserDao(): UserDao

    companion object {
        @Volatile
        private var instance: ProductDatabase? = null


        fun getInstance(context: Context): ProductDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context,
                    ProductDatabase::class.java,
                    "product_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { createdInstance ->
                        instance = createdInstance
                    }
            }
        }


    }
}