package com.example.trial3

import androidx.room.TypeConverter
import java.util.*
class Convertors {

    @TypeConverter
    fun fromDateToLong(value:Date):Long{
        return value.time
    }

    @TypeConverter
    fun fromLongtoDate(value: Long): Date{
        return Date(value)
    }
}
