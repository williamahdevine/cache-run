package com.example.cacherun

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.ar.sceneform.ux.ArFragment
import android.view.View
import com.google.ar.core.*


class MainActivity : AppCompatActivity() {

//    Location targetLocation = new Location("");//provider name is unnecessary
//    targetLocation.setLatitude(0.0d);//your coords of course
//    targetLocation.setLongitude(0.0d);
//
//    float distanceInMeters =  targetLocation.distanceTo(myLocation);
//    hardCodedLocation = Location("")
//        hardCodedLocation.latitude = 37.4
//        hardCodedLocation.longitude = -122.0

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var hardCodedLocation: Location
    private lateinit var pointerDrawable: PointerDrawable

    private lateinit var arFragement: ArFragment

    private var requestingLocationUpdates = false
    private var isHitting = false
    private var isTracking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        arFragement = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment

        pointerDrawable = PointerDrawable()

        arFragement.getArSceneView().getScene().addOnUpdateListener({ frameTime ->
            arFragement.onUpdate(frameTime)
            onUpdate()
        })

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?: return
                for (location in locationResult.locations) {
                    print("hello")
                }
            }
        }
    }

    private fun onUpdate() {
        val hasTrackingChanged = updateTracking()
        val contentView: View = findViewById(R.id.content)

        if (hasTrackingChanged) {
            contentView.overlay.add(pointerDrawable)
        } else {
            contentView.overlay.remove(pointerDrawable)
        }

        contentView.invalidate()

        if (isTracking) {
            val hitTestChanged = updateHitTest()
            if (hitTestChanged) {
                pointerDrawable.enabled = isHitting
                contentView.invalidate()
            }
        }
    }

    private fun updateTracking(): Boolean {
        var frame: Frame? = arFragement.arSceneView.arFrame
        var wasTracking = isTracking

        isTracking = ((frame != null) && (frame.camera.trackingState == TrackingState.TRACKING))

        return isTracking == wasTracking
    }

    private fun updateHitTest(): Boolean {
        var frame: Frame? = arFragement.arSceneView.arFrame
        val centerPoint = getScreenCenter()
        val hits: List<HitResult>
        var wasHitting = isHitting
        isHitting = false

        if (frame != null) {
            hits = frame.hitTest(centerPoint.x.toFloat(), centerPoint.y.toFloat())
            for (hit in hits) {
                var trackable = hit.trackable
                if (trackable is Plane && (trackable).isPoseInPolygon(hit.hitPose)) {
                    isHitting = true
                    break
                }
            }
        }
        return wasHitting != isHitting
    }

    private fun getScreenCenter(): android.graphics.Point {
        val view = findViewById<View>(android.R.id.content)
        return android.graphics.Point(view.width / 2, view.height /2)
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
