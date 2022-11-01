package com.example.trial3

import android.app.Application

class App: Application() {
     var bpercent:Int=0
     var isCharging:Boolean= false
    companion object{
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance=this
    }
}