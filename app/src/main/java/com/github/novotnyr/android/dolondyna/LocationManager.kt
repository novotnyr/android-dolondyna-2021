package com.github.novotnyr.android.dolondyna

import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.concurrent.TimeUnit

val LONDON = Location("").apply {
    latitude = 51.509865
    longitude = -0.118092
}

class LocationManager(
    context: Context,
    locationListener: (Location) -> Unit = {}
) {
    private val locationProvider =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest.create().apply {
        interval = TimeUnit.SECONDS.toMillis(5)
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val locationCallback: LocationCallback =
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                locationListener(result.lastLocation)
            }
        }

    fun start() {
        try {
            locationProvider.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            Log.e("LocationService", "Location access denied", e)
        }
    }

    fun stop() {
        locationProvider.removeLocationUpdates(locationCallback)
    }
}