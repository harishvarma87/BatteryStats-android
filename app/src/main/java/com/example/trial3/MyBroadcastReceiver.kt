package com.example.trial3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi


class MyBroadcastReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            val serviceIntent = Intent(
                context,
                MyForegroundService::class.java
            )
            context.startForegroundService(serviceIntent);
        }
    }
}