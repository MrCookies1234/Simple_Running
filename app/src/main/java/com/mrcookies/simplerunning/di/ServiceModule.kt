package com.mrcookies.simplerunning.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.mrcookies.simplerunning.R
import com.mrcookies.simplerunning.core.Constants
import com.mrcookies.simplerunning.services.TrackingService
import com.mrcookies.simplerunning.ui.view.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Singleton

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    @ServiceScoped
    fun provideFusedLocationProviderClient(@ApplicationContext app : Context) : FusedLocationProviderClient{
        return FusedLocationProviderClient(app)
    }

    @Provides
    @ServiceScoped
    fun provideMainActivityPendingIntent(@ApplicationContext app : Context) : PendingIntent{
        return PendingIntent.getActivity(app,
            0,
            Intent(app, MainActivity::class.java).also {
                it.action = Constants.ACTION_SHOW_TRACKING_ACTIVITY
            },
            PendingIntent.FLAG_UPDATE_CURRENT)
    }

    @Provides
    @ServiceScoped
    fun provideBaseNotificationBuilder(@ApplicationContext app: Context, pendingIntent: PendingIntent): NotificationCompat.Builder{
        return NotificationCompat.Builder(app,Constants.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_icon_action_color)
            .setContentTitle("Simple Running")
            .setContentText("00:00:00")
            .setContentIntent(pendingIntent)
    }
}