package com.example.trial3

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class repository(private val entrydao: entrydao) {
    val allentries: Flow<List<entry>> = entrydao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(entry: entry){
        entrydao.insert(entry)
    }
}