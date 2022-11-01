package com.example.trial3

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {


    lateinit var database: AppDatabase

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        /*val btn: Button= findViewById(R.id.button)*/
        val btn2: Button= findViewById(R.id.button2)
        val btn3: Button= findViewById(R.id.button3)
        if (!foregroundServiceRunning()) {
            val serviceIntent = Intent(
                this,
                MyForegroundService::class.java
            )
            startForegroundService(serviceIntent)
        }
        database= AppDatabase.getDataBase(App.instance)
        var c:Int=0
        var t:List<Date> = listOf<Date>()
        var p: List<Int> = listOf<Int>()
        var s: List<String> = listOf<String>()
        /*btn.setOnClickListener {
            GlobalScope.launch {
                database.entrydao().deleteAll()
            }
        }*/
        var date1: String = "Fri Oct 28 00:01:20 GMT+05:30 2022"
        //var date2: String = "Fri Oct 28 00:00:10 GMT+05:30 2022"

        //get charge cycles
        btn2.setOnClickListener {

            //extracting data from database
            GlobalScope.launch {
                t= database.entrydao().getTime()
                p=database.entrydao().getPercent()
                s=database.entrydao().getStatus()
            }
            var n: Int= p.count()
            var res: ArrayList<String> = ArrayList<String>()

            var diff: Int=0
            var prev_time: Date

            var time_diff: String
            var last_time: String
            var first_time: String
            var time: String="00:00:00"
            if(t.count()>0)
            {
                res.add(".........................on "+t[0].toString().slice(0..9)+".............................")

                prev_time=t[0]
                for(i in 1..n-1)
                {

                    //Log.d("harish", "onCreate: ${t[i]} | ${p[i]} | ${s[i]}")
                    // calculate the discharge
                    if(p[i]<p[i-1] && t[i].toString().slice(11..12)==t[i-1].toString().slice(11..12))
                    {
                        diff=diff+(p[i-1]-p[i])
                        time_diff=diff(t[i].toString(), t[i-1].toString())
                        time= add(time, time_diff)
                        prev_time=t[i]

                    }
                    Log.d("harish", "onCreate: ${t[i]} | ${p[i]} | $time")
                    // Add the new date
                    if(i!=n-1 && t[i].toString().slice(0..9)!=t[i+1].toString().slice(0..9))
                    {
                        if(p[i+1]<p[i])
                        {
                            last_time=diff("Fri Oct 28 "+t[i+1].toString().slice(11..12)+":00:00 GMT+05:30 2022",t[i].toString())
                            time=add(last_time,time)

                        }

                        if(diff==0)
                        {
                            res.add("from "+t[i].toString().slice(11..12)+":00 to "+t[i].toString().slice(11..15)+" there is no discharge")
                        }
                        else{
                            res.add("from "+t[i].toString().slice(11..12)+":00 to "+t[i].toString().slice(11..15)+" --> discharged percentage is "+diff+"% time taken is "+time.slice(3..4)+" minutes"+time.slice(6..7)+" seconds")
                        }
                        res.add(".........................on "+t[i].toString().slice(0..9)+".............................")
                        diff = 0

                    }
                    // Add the new time period
                    else if (i!=n-1 && t[i].toString().slice(11..12)!=t[i+1].toString().slice(11..12))
                    {
                        if(p[i+1]<p[i])
                        {
                            last_time=diff("Fri Oct 28 "+t[i+1].toString().slice(11..12)+":00:00 GMT+05:30 2022",t[i].toString())
                            time=add(last_time,time)
                            Log.d("varma", "onCreate: $time")
                        }
                        if(diff==0)
                        {
                            res.add("from "+t[i].toString().slice(11..12)+":00 to "+t[i+1].toString().slice(11..12)+":00 there is no discharge")
                        }
                        else
                        {
                        res.add("from "+t[i].toString().slice(11..12)+":00 to "+t[i+1].toString().slice(11..12)+":00 --> discharged percentage is "+diff+"% time taken is "+time.slice(3..4)+" minutes "+time.slice(6..7)+" seconds")
                        }

                        diff=0


                    }


                    if (i!=n-1 && t[i].toString().slice(11..12)!=t[i+1].toString().slice(11..12))
                    {
                        time="00:"+t[i+1].toString().slice(14..18)
                        time_diff="00:00:00"
                    }

                }

            }
            val adapter = ArrayAdapter(this,
                R.layout.listview_item, res)
            val listView: ListView = findViewById(R.id.listview_1)
            listView.setAdapter(adapter)
        }
        //Get charge pattern
        btn3.setOnClickListener {
            var res: ArrayList<String> = ArrayList<String>()
            GlobalScope.launch {
                t= database.entrydao().getTime()
                p=database.entrydao().getPercent()
                s=database.entrydao().getStatus()
            }
            var n: Int= p.count()
            var flag: Int =0
            var flagn: Int =0
            var count: Int=0
            var init_p: Int=100
            var final_p: Int=100
            var rcount: String="Spot"
            var diff: String
            if(t.count()>0)
            {
                var stime: String = t[0].toString().slice(11..18)
                var etime: String = t[0].toString().slice(11..18)
                var stimen: String = t[0].toString().slice(11..18)
                var etimen: String = t[0].toString().slice(11..18)
                res.add(".........................on "+t[0].toString().slice(0..9)+".............................")
                for (i in 1..n-1)
                {
                    if(t[i].toString().slice(0..9)!=t[i-1].toString().slice(0..9))
                    {
                        res.add(".........................on "+t[i].toString().slice(0..9)+".............................")
                        stime= t[0].toString().slice(11..18)
                        etime = t[0].toString().slice(11..18)
                        stimen = t[0].toString().slice(11..18)
                        etimen = t[0].toString().slice(11..18)
                    }
                    //checking if overcharging of battery has started or not
                    if(p[i]==100 && s[i]=="true" && flag==0 /*&& t[i].toString().slice(14..15)!=t[i-1].toString().slice(14..15)*/)
                    {
                        flagn=0
                        count=0
                        flag=1
                        final_p=p[i]
                        stime= t[i].toString()
                    }
                    else if(p[i]!=100 && s[i]=="true" && flagn==0)
                    {
                        init_p=p[i]
                        flagn=1
                        stimen= t[i].toString().slice(11..18)
                    }
                    /*if(flagn==1 && s[i]=="true")
                    {
                        count=count+1
                        etimen= t[i].toString().slice(11..18)
                        final_p=p[i]
                    }*/

                    else if(flagn==1 && s[i]=="false" && flag==0)
                    {
                        etimen= t[i].toString().slice(11..18)
                        final_p=p[i]
                        flagn=0
                        count=0
                        res.add("Charged from "+stimen+" to "+etimen+" from "+init_p+"% to "+final_p+"% and count is Spotcount")
                    }
                    //condition when overcharging is completed
                    else if(flag==1 && s[i]=="false" && flagn==0)
                    {
                        etime=t[i].toString()
                        flag=0
                        final_p=100
                        diff= diff(etime, stime)
                        if (diff.slice(3..4).toInt()>=30 || diff.slice(0..1).toInt()>0)
                            rcount="BadCount"
                        else
                            rcount="OptimalCount"
                        res.add("Overcharged from "+stime.toString().slice(11..18)+" to "+etime.toString().slice(11..18)+" from "+init_p+"% to "+final_p+"%and count is "+rcount)
                    }
                }
            }
            val adapter = ArrayAdapter(this,
                R.layout.listview_item, res)
            val listView: ListView = findViewById(R.id.listview_1)
            listView.setAdapter(adapter)
        }




        //this.registerReceiver(myBroadcastReciever, intentfilter)
    }

    fun foregroundServiceRunning(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
            if (MyForegroundService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }
    fun diff(date1: String, date2: String): String
    {
        var h1: Int= date1.slice(11..12).toInt()
        var h2: Int= date2.slice(11..12).toInt()
        var m1: Int= date1.slice(14..15).toInt()
        var m2: Int= date2.slice(14..15).toInt()
        var s1: Int= date1.slice(17..18).toInt()
        var s2: Int= date2.slice(17..18).toInt()
        var h3: Int
        var m3: Int
        var s3: Int

        var hr: String
        var mr: String
        var sr: String
        while(s2>s1)
        {
            m1=m1-1
            s1=s1+60
        }
        s3= s1-s2
        sr=s3.toString()
        if(s3<10){ sr= "0"+sr}
        while(m2>m1)
        {
            h1=h1-1
            m1=m1+60
        }
        m3=m1-m2
        mr=m3.toString()
        if(m3<10){ mr= "0"+mr}
        h3=h1-h2
        hr=h3.toString()
        if(h3<10){ hr= "0"+hr}
        var resu: String= hr+":"+mr+":"+sr
        return resu
    }

    fun add(date1: String, date2: String): String
    {
        var h1: Int= date1.slice(0..1).toInt()
        var h2: Int= date2.slice(0..1).toInt()
        var m1: Int= date1.slice(3..4).toInt()
        var m2: Int= date2.slice(3..4).toInt()
        var s1: Int= date1.slice(6..7).toInt()
        var s2: Int= date2.slice(6..7).toInt()
        var h3: Int= h1+h2
        var m3: Int= m1+m2
        var s3: Int= s1+s2
        var hr: String
        var mr: String
        var sr: String
        if(s3>60)
        {
            m3=m3+1
            s3=s3%60
        }
        sr=s3.toString()
        if(s3<10){ sr= "0"+sr}
        if(m3>60)
        {
            h3=h3+1
            m3=m3%60
        }
        mr=m3.toString()
        if(m3<10){ mr= "0"+mr}
        hr=h3.toString()
        if(h3<10){ hr= "0"+hr}
        var resu: String= hr+":"+mr+":"+sr
        return resu
    }


}
