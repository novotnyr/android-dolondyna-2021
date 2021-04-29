package com.github.novotnyr.android.dolondyna

import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.Intent.ACTION_DELETE
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

const val TAG = "LocationService"

const val CHANNEL_ID = "0"

const val NOTIFICATION_ID = 1

class LocationService : Service() {
    private lateinit var locationManager: LocationManager

    private lateinit var notificationManager: NotificationManagerCompat

    override fun onCreate() {
        super.onCreate()
        locationManager = LocationManager(this) { location ->
            val message =
                "%.2f:%.2f".format(location.latitude, location.longitude)
            Log.i(TAG, message)
            notificationManager.notify(NOTIFICATION_ID, createNotification(message))
        }
        notificationManager = NotificationManagerCompat.from(this)
        createNotificationChannel()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        if (intent?.action == ACTION_DELETE) {
            stopSelf()
        } else {
            startForeground(NOTIFICATION_ID, createNotification())
            locationManager.start()
        }
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

    private fun createNotification(location: String = "Neznáma poloha") =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Aktuálna poloha")
            .setContentText(location)
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .addAction(android.R.drawable.ic_delete,
                "Zastaviť",
                getStopPendingIntent()
            )
            .build()

    private fun getStopPendingIntent() =
        Intent(this, LocationService::class.java)
            .setAction(ACTION_DELETE)
            .let {
                PendingIntent.getService(this, 0, it, 0)
            }
}