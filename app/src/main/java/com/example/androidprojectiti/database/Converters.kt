package com.example.androidprojectiti.database

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    public fun fromAnyToString(any: Any): String {
        return Gson().toJson(any)
    }

    @TypeConverter
    public fun fromStringToDimensions(str: String): Any {
        return Gson().fromJson(str, Any::class.java)
    }
}