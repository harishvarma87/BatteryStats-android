package com.example.trial3

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.*


@Dao
interface entrydao{
    @Query("SELECT * FROM data")
    fun getAll(): Flow<List<entry>>

    @Query("SELECT time from data ")
    fun getTime(): List<Date>

    @Query("SELECT percent from data ")
    fun getPercent(): List<Int>

    @Query("SELECT status from data ")
    fun getStatus(): List<String>

    @Query("SELECT count(*) from data")
    fun getCount(): Int

    @Insert()
    fun insert(entry: entry)

    @Query("delete from data")
    fun deleteAll()

}