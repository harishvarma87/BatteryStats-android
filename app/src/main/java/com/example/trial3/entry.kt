package com.example.trial3

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*


@Entity(tableName="data")
data class entry(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val percent: Int,
    val status:String,
    val time: Date
)
