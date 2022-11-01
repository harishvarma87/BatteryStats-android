package com.example.trial3

import android.content.BroadcastReceiver
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.internal.synchronized


@Database(entities = [entry :: class], version=1)
@TypeConverters(Convertors::class)
abstract class AppDatabase: RoomDatabase (){

    abstract fun entrydao() : entrydao
    companion object{
        private var INSTANCE: AppDatabase?= null
        fun getDataBase(context: Context= App.instance): AppDatabase{
            if(INSTANCE==null){
                kotlin.synchronized(this){
                    INSTANCE=Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java,
                        "datadb").build()
                }
            }
            return INSTANCE!!
        }


    }

}