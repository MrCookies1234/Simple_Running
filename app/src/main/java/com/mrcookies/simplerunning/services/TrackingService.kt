package com.mrcookies.simplerunning.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.mrcookies.simplerunning.R
import com.mrcookies.simplerunning.core.Constants
import com.mrcookies.simplerunning.core.PermissionsUtility
import com.mrcookies.simplerunning.core.TrackingUtility
import com.mrcookies.simplerunning.ui.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias PolyLine = MutableList<LatLng>
typealias PolyLines = MutableList<PolyLine>

@AndroidEntryPoint
class TrackingService : LifecycleService() {

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var baseNotificationBuilder : NotificationCompat.Builder

    lateinit var currentNotificationBuilder : NotificationCompat.Builder

    private var isFirstRun = true
    private var isServiceKilled = false


    private var isTimerEnabled = false
    private var lapTime= 0L
    private var timeRun = 0L
    private var timeStarted = 0L
    private var lastSecondTimeStamp = 0L

    private val timeInSeconds = MutableLiveData<Long>()

    companion object{
        val isTracking = MutableLiveData<Boolean>()
        val pointsInMap = MutableLiveData<PolyLines>()
        val timeinMillis = MutableLiveData<Long>()
    }

    //I don't get why locationCallback isn't an interface
    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if(isTracking.value!!){
                result.locations.let {
                    for(location in it){
                        addPoint(location)
                        Log.d("LOCATION: ", "${location.latitude}, ${location.longitude}")
                    }
                }
            }
        }
    }

    @SuppressLint("VisibleForTests")
    override fun onCreate() {
        super.onCreate()
        currentNotificationBuilder = baseNotificationBuilder
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this) {
            updateLocationTracking(it)
            updateNotificationTrackingState(it)
        }

    }

    private fun postInitialValues(){
        isTracking.postValue(false)
        pointsInMap.postValue(mutableListOf())
        timeInSeconds.postValue(0L)
        timeinMillis.postValue(0L)
    }

    private fun startTimer(){
        addEmptyPolyline()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true

        CoroutineScope(Dispatchers.Main).launch {

            while(isTracking.value!!){
                lapTime = System.currentTimeMillis() - timeStarted
                timeinMillis.postValue(timeRun + lapTime)
                if(timeinMillis.value!! >= lastSecondTimeStamp + 1000L){
                    timeInSeconds.postValue(timeInSeconds.value!! + 1)
                    lastSecondTimeStamp += 1000L
                }
                delay(Constants.TIMER_UPDATE_INTERVAL)
            }

            timeRun += lapTime
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action) {
                Constants.ACTION_START_RESUME_SERVICE -> {
                    if(isFirstRun){
                        startForegroundService()
                        isFirstRun = false
                    }else{
                        Log.d("SERVICE COMMAND: ", "ALREADY STARTED")
                        startTimer()
                    }
                }
                Constants.ACTION_PAUSE_SERVICE -> {
                    pauseService()
                }
                Constants.ACTION_STOP_SERVICE -> {
                    killService()
                }
                else ->{
                    Log.d("ERROR SERVICE COMMAND","ERROR")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun killService(){
        isServiceKilled = true
        isFirstRun = true
        pauseService()
        postInitialValues()
        stopForeground(true)
        stopSelf()
    }

    private fun pauseService(){
        isTracking.postValue(false)
        isTimerEnabled = false
    }

    private fun updateNotificationTrackingState(isTracking: Boolean) {
        val notificationActionText = if (isTracking) "Pause" else "Resume"
        val pendingIntent = if (isTracking) {
            val pauseIntent = Intent(this, TrackingService::class.java).apply {
                action = Constants.ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
                val resumeIntent = Intent(this,TrackingService::class.java).apply {
                    action = Constants.ACTION_START_RESUME_SERVICE
                }
            PendingIntent.getService(this,2,resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        currentNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(currentNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }

        if(!isServiceKilled){
            currentNotificationBuilder = baseNotificationBuilder.addAction(R.drawable.ic_icon_action_color, notificationActionText, pendingIntent)
            notificationManager.notify(Constants.NOTIFICATION_ID, currentNotificationBuilder.build())
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking : Boolean){
        if(!isTracking){
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            return
        }

        if(PermissionsUtility.hasLocationPermissions(this)){
            val request = LocationRequest.create().apply {
                interval = Constants.MAX_LOCATION_UPDATE_INTERVAL
                fastestInterval = Constants.MIN_LOCATION_UPDATE_INTERVAL
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationProviderClient.requestLocationUpdates(request,
                locationCallback,
                Looper.getMainLooper())
        }
    }

    private fun addPoint(location : Location?){
        location?.let {
            val pos = LatLng(location.latitude,location.longitude)
            pointsInMap.value?.apply {
                last().add(pos)
                pointsInMap.postValue(this)
            }
        }
    }

    private fun addEmptyPolyline(){
        pointsInMap.value?.apply {
            add(mutableListOf())
            pointsInMap.postValue(this)
        }?: pointsInMap.postValue(mutableListOf(mutableListOf()))
    }

    private fun startForegroundService(){
        startTimer()
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Starting from api O all notifications need to be attached to a notification channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        startForeground(Constants.NOTIFICATION_ID, baseNotificationBuilder.build())

        timeInSeconds.observe(this, Observer {
            if(!isServiceKilled){
                val notification = currentNotificationBuilder.setContentText(TrackingUtility.getFormattedStopWatchTime(it * 1000L))
                notificationManager.notify(Constants.NOTIFICATION_ID, notification.build())
            }
        })
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