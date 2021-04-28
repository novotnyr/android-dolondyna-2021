package com.github.novotnyr.android.dolondyna

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED

class MainActivity : AppCompatActivity() {
    private val locationPermission =
        registerForActivityResult(RequestPermission()) { granted ->
            if (granted) {
                showLocation()
            } else {
                TODO("Povolenie bolo zamietnuté")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onCompassImageViewClick(view: View) {
        when (checkSelfPermission(this, ACCESS_FINE_LOCATION)) {
            PERMISSION_GRANTED -> showLocation()
            else -> locationPermission.launch(ACCESS_FINE_LOCATION)
        }
    }

    fun showLocation() {
        TODO("Zobraziť polohu")
    }
}