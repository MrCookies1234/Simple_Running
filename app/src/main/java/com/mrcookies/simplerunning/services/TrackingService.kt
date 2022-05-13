package com.mrcookies.simplerunning.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.mrcookies.simplerunning.R
import com.mrcookies.simplerunning.core.Constants
import com.mrcookies.simplerunning.ui.view.MainActivity

class TrackingService : LifecycleService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action) {
                Constants.ACTION_START_RESUME_SERVICE -> {
                    startForegroundService()
                }
                Constants.ACTION_PAUSE_SERVICE -> {
                    Log.d("SERVICE COMMAND: ", "PAUSE")
                }
                Constants.ACTION_STOP_SERVICE -> {
                    Log.d("SERVICE COMMAND: ", "STOP")
                }
                else ->{
                    Log.d("ERROR SERVICE COMMAND","ERROR")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Starting from api O all notifications need to be attached to a notification channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this,Constants.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_icon_action_color)
            .setContentTitle("Simple Running")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(Constants.NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getMainActivityPendingIntent() : PendingIntent?{
        return PendingIntent.getActivity(this,
            0,
        Intent(this,MainActivity::class.java).also {
            it.action = Constants.ACTION_SHOW_TRACKING_ACTIVITY
        },
        PendingIntent.FLAG_UPDATE_CURRENT)
    }

    //Starting from api O all notifications need to be attached to a notification channel
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW)

        notificationManager.createNotificationChannel(channel)
    }
}