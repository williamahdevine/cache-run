package com.example.cacherun

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnSuccessListener

class MainActivity : AppCompatActivity() {

//    Location targetLocation = new Location("");//provider name is unnecessary
//    targetLocation.setLatitude(0.0d);//your coords of course
//    targetLocation.setLongitude(0.0d);
//
//    float distanceInMeters =  targetLocation.distanceTo(myLocation);

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var hardCodedLocation: Location

    private var requestingLocationUpdates = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        var tv: TextView = findViewById(R.id.temp_view)
        var s = ""
        var lat = ""
        var lon = ""

        hardCodedLocation = Location("")
        hardCodedLocation.latitude = 37.4
        hardCodedLocation.longitude = -122.0


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?: return
                for (location in locationResult.locations) {
                    lat = location.latitude.toString()
                    lon = location.longitude.toString()
                    s = "$lat \t $lon"
//                    tv.text = location.distanceTo(hardCodedLocation).toString()
                    tv.text = s
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        if (!hasLocationPermissions())
            requestPermissions()
        else
            getAddress()
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        requestingLocationUpdates = false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode != REQUEST_PERMISSIONS_REQUEST_CODE) return

        if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            getAddress()
    }

    private fun getAddress() {
        fusedLocationClient.lastLocation.addOnSuccessListener(this, OnSuccessListener { location ->
            if (location != null) {
                createLocationRequest()
                startLocationUpdates()
            }
        }).addOnFailureListener(this) { e -> Log.w("getLastLocationFailure: onFailure", e)}
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        requestingLocationUpdates = true
    }


    fun hasLocationPermissions() = hasFineLocationPermission() && hasCoarseLocationPermissions()
    fun hasFineLocationPermission() = ActivityCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    fun hasCoarseLocationPermissions() = ActivityCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED



}
