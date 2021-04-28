package com.github.novotnyr.android.dolondyna

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

const val TAG = "LocationService"

class LocationService: Service() {
    private lateinit var locationManager: LocationManager

    override fun onCreate() {
        super.onCreate()
        locationManager = LocationManager(this) { location ->
            val message = "%.2f:%.2f".format(location.latitude, location.longitude)
            Log.i(TAG, message)
        }
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
}