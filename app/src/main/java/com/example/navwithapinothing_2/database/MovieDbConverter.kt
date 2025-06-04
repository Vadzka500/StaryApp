package com.example.navwithapinothing_2.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @Author: Vadim
 * @Date: 04.06.2025
 */
class MovieDbConverter {
    val gson = Gson()

    @TypeConverter
    fun listCollectionsToString(list : List<String>?) : String?{
        return gson.toJson(list)
    }

    @TypeConverter
    fun toListClipboardsFromString(listOfClipboardsStr : String?) : List<String>?{
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson<List<String>>(listOfClipboardsStr, type)
    }
}