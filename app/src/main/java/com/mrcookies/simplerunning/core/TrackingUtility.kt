package com.mrcookies.simplerunning.core

import android.location.Location
import com.mrcookies.simplerunning.services.PolyLine
import java.util.concurrent.TimeUnit


object TrackingUtility {

    fun getFormattedStopWatchTime(ms :Long, includeMillis :Boolean = false) : String {
        var msCopy= ms

        val hours = TimeUnit.MILLISECONDS.toHours(ms)
        msCopy -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(msCopy)
        msCopy -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(msCopy)

        if(!includeMillis){
            return "${if (hours < 10) "0" else ""}$hours:"+
                    "${if (minutes<10) "0" else ""}$minutes:"+
                    "${if (seconds<10) "0" else ""}$seconds"
        }

        msCopy -= TimeUnit.SECONDS.toMillis(seconds)
        msCopy /= 10

        return "${if (hours<10) "0" else ""}$hours:"+
                "${if (minutes<10) "0" else ""}$minutes:"+
                "${if (seconds<10) "0" else ""}$seconds:"+
                "${if (msCopy<10) "0" else ""}$msCopy"
    }

    fun calculatePolylineLength(polyLine: PolyLine) : Float{
        var distance = 0f
        for (i in 0..polyLine.size -2){
            val pos1 = polyLine[i]
            val pos2= polyLine[i+1]

            val result = FloatArray(1)

            Location.distanceBetween(pos1.latitude,pos1.longitude,pos2.latitude,pos2.longitude,result)

            distance += result[0]
        }
        return distance
    }
}