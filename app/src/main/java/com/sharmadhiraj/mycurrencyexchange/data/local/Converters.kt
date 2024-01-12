package com.sharmadhiraj.mycurrencyexchange.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {

    @TypeConverter
    fun fromString(value: String?): Map<String, Double>? {
        if (value == null) {
            return null
        }
        val type = object : TypeToken<Map<String, Double>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun toString(value: Map<String, Double>?): String? {
        if (value == null) {
            return null
        }
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}