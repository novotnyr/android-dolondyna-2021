package com.github.novotnyr.android.dolondyna

import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationManagerCompat

const val TAG = "LocationService"

const val CHANNEL_ID = "0"

class LocationService : Service() {
    private lateinit var locationManager: LocationManager

    private lateinit var notificationManager: NotificationManagerCompat

    override fun onCreate() {
        super.onCreate()
        locationManager = LocationManager(this) { location ->
            val message =
                "%.2f:%.2f".format(location.latitude, location.longitude)
            Log.i(TAG, message)
        }
        notificationManager = NotificationManagerCompat.from(this)
        createNotificationChannel()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        locationManager.start()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        locationManager.stop()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        NotificationChannel(
            CHANNEL_ID, "Aktuálna poloha", IMPORTANCE_DEFAULT
        ).let(notificationManager::createNotificationChannel)
    }
}