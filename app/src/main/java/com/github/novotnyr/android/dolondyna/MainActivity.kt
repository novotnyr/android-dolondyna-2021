package com.github.novotnyr.android.dolondyna

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {
    private val locationPermission =
        registerForActivityResult(RequestPermission()) { granted ->
            if (granted) {
                showLocation()
            } else {
                TODO("Povolenie bolo zamietnuté")
            }
        }

    private lateinit var locationProvider: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationProvider = LocationServices.getFusedLocationProviderClient(this)
    }

    fun onCompassImageViewClick(view: View) {
        when (checkSelfPermission(this, ACCESS_FINE_LOCATION)) {
            PERMISSION_GRANTED -> showLocation()
            else -> locationPermission.launch(ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    fun showLocation() {
        locationProvider.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                title = "Poloha: %.2f:%.2f".format(location.latitude, location.longitude)
            }
        }
        startService(Intent(this, LocationService::class.java))
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        super.onPause()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun update(location: Location) {
        Log.i("ACTIVITY", location.toString())
    }
}