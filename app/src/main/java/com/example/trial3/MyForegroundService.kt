package com.example.trial3

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.room.TypeConverters
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class MyForegroundService : Service() {
    lateinit var database: AppDatabase
    @RequiresApi(Build.VERSION_CODES.O)
    @Override
    @TypeConverters(Convertors::class)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        database= AppDatabase.getDataBase(App.instance)
        var p= App()
        var prev:Int=0
        Thread {
            while (true) {
                val bm= applicationContext.getSystemService(BATTERY_SERVICE) as BatteryManager
                p.bpercent=bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                val batterystatus: Intent?= IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter->
                    applicationContext.registerReceiver(null,ifilter)
                }
                val status: Int= batterystatus?.getIntExtra(BatteryManager.EXTRA_STATUS,-1)?:-1
                p.isCharging=status== BatteryManager.BATTERY_STATUS_CHARGING || status== BatteryManager.BATTERY_STATUS_FULL
                val s= p.isCharging
                var l:List<Date> = listOf<Date>()
                var c:Int=0
                if(p.bpercent!= prev ) {
                    GlobalScope.launch {
                        database.entrydao().insert(entry(0, p.bpercent, "$s", Date()))
                    }
                    Log.d("harish", "onStartCommand: added item to db")
                }
                prev=p.bpercent
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
        val CHANNELID = "Foreground Service ID"
        val channel = NotificationChannel(
            CHANNELID,
            CHANNELID,
            NotificationManager.IMPORTANCE_LOW
        )

        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        val notification: Notification.Builder = Notification.Builder(this, CHANNELID)
            .setContentText("Service is running")
            .setContentTitle("Service enabled")


        startForeground(1001, notification.build())
        return super.onStartCommand(intent, flags, startId)

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}