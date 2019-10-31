package com.example.cacherun

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.ar.sceneform.ux.ArFragment
import android.widget.Toast
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.TransformableNode


class MainActivity : AppCompatActivity() {

//    Location targetLocation = new Location("");//provider name is unnecessary
//    targetLocation.setLatitude(0.0d);//your coords of course
//    targetLocation.setLongitude(0.0d);
//
//    float distanceInMeters =  targetLocation.distanceTo(myLocation);
//    hardCodedLocation = Location("")
//        hardCodedLocation.latitude = 37.4
//        hardCodedLocation.longitude = -122.0

//    44.673497, -63.614482

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var hardCodedLocation: Location
    private lateinit var pointerDrawable: PointerDrawable
    private lateinit var piggyRenderable: ModelRenderable

    private lateinit var arFragment: ArFragment

    private var requestingLocationUpdates = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var deltaD = findViewById<TextView>(R.id.delta_d)
        var curLatLon = findViewById<TextView>(R.id.cur_latlon)
        var goalLatLon = findViewById<TextView>(R.id.goal_latlon)

        hardCodedLocation = Location("")
        hardCodedLocation.latitude = 44.673497
        hardCodedLocation.longitude = -63.614482




        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        arFragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment

        ModelRenderable.builder()
            .setSource(this, R.raw.piggybank)
            .build()
            .thenAccept{ renderable -> piggyRenderable = renderable }
            .exceptionally { t: Throwable? ->
                var toast = Toast.makeText(this, "Unable to load piggy renderable",
                    Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                null

            }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?: return
                for (location in locationResult.locations) {
                    deltaD.text = "Delta D: " + location.distanceTo(hardCodedLocation).toString()
                    curLatLon.text = "Cur Loc: " + location.latitude + ",\t" + location.longitude
                    goalLatLon.text = "Goal Loc: " + hardCodedLocation.latitude + ",\t" + hardCodedLocation.longitude

                }
            }
        }

        arFragment.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane, motionEvent: MotionEvent ->
            if (piggyRenderable == null) {
                // @TODO Do something
            }

            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)

            val piggy = TransformableNode(arFragment.transformationSystem)
            piggy.setParent(anchorNode)
            piggy.renderable = piggyRenderable
            piggy.select()
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

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
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

    private fun hasLocationPermissions() = hasFineLocationPermission() && hasCoarseLocationPermissions()
    private fun hasFineLocationPermission() = ActivityCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    private fun hasCoarseLocationPermissions() = ActivityCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
}
