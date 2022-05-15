package com.example.assignment3

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.util.jar.Manifest

class GeolocationActivity : AppCompatActivity() {
    private var REQUEST_LOCATION_CODE = 101
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var longTextView: TextView
    private lateinit var latTextView: TextView
    private lateinit var whereBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geolocation)

        latTextView = findViewById(R.id.latitudeText)
        longTextView = findViewById(R.id.longitudeText)
        whereBtn = findViewById(R.id.btnWhere)

        whereBtn.setOnClickListener {
            where()
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun where() {
        // Check if location is enabled
        if (!(getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            noGPSAlert()
        }
        // Location enabled - is location granted?
        else if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            noPermissionsAlert()
        }
        else {
            getLocation()
        }
    }

    private fun noGPSAlert() {
        AlertDialog.Builder(this)
            .setTitle("Enable Location")
            .setMessage("Please enable location settings to use app")
            .setPositiveButton("Location settings") {
                    _, _ -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            } .setNegativeButton("Cancel") {
                    _, _ ->

            } .show()
    }

    private fun noPermissionsAlert() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf( android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_LOCATION_CODE
        )
    }

    override fun onRequestPermissionsResult( requestCode: Int, permissions: Array<out String>, grantResults: IntArray ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_LOCATION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
            getLocation()
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                location: Location? -> if (location == null) {
                    Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
                }
        else {
            longTextView.text = "Longitude: " + location.longitude.toString()
            latTextView.text = "Latitude: " + location.latitude.toString()
        }
        }
    }
}