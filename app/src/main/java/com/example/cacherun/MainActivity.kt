package com.example.cacherun

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.ar.sceneform.ux.ArFragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    //private lateinit var hardCodedLocation: Location
    private lateinit var pointerDrawable: PointerDrawable
    private lateinit var piggyRenderable: ModelRenderable
    private lateinit var pizzaRenderable: ModelRenderable
    private lateinit var bookRenderable: ModelRenderable
    val piggy= Coupon("piggy")
    val pizza= Coupon("pizza")
    val book= Coupon("book")

    lateinit var deltaD: TextView
    lateinit var curLatLon: TextView
    lateinit var goalLatLon: TextView

    private lateinit var arFragment: ArFragment

    private var requestingLocationUpdates = false
    private var canSetModel = false

    // Set this to a high number if you want to enable placing of "coupons"
    // Set this to a low number if you want to disable placing of "coupons"
    private var distanceThreshold = 1000000.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // display of locations for debugging purposes
//        deltaD = findViewById(R.id.delta_d)
//        curLatLon = findViewById(R.id.cur_latlon)
//        goalLatLon = findViewById(R.id.goal_latlon)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        arFragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment

        doLocationCallback()
        buildCoupons()
        doSetOnTapArPlaneListener()


        /********************************/
        val posts: ArrayList<String> = ArrayList()

        for (i in 1..10) {
            posts.add("Coupon # $i")
        }

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = CouponAdapter(posts)
        /********************************/

    }

    //builds all coupons and places them appropriately
    private fun buildCoupons() {

        //makes all renderable models
        makeRenderable()

        //val piggy= Coupon("piggy")
        piggy.hardCodedLocation.latitude= 44.636
        piggy.hardCodedLocation.longitude = -63.591
       // piggy.Renderable= piggyRenderable

//        val pizza= Coupon("pizza")
        pizza.hardCodedLocation.latitude= 44.6365
        pizza.hardCodedLocation.longitude = -63.592
        //pizza.Renderable= pizzaRenderable

//        val book= Coupon("book")
        book.hardCodedLocation.latitude= 44.6369
        book.hardCodedLocation.longitude = -63.593
        //book.Renderable= bookRenderable
    }

    //takes all sfb files and builds renderable models
    private fun makeRenderable(){
        ModelRenderable.builder()
            .setSource(this, R.raw.piggybank)
            .build()
            .thenAccept { renderable -> piggyRenderable = renderable }
            .exceptionally { t: Throwable? ->
                var toast = Toast.makeText(
                    this, "Unable to load piggy renderable",
                    Toast.LENGTH_LONG
                )
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                null
            }

        ModelRenderable.builder()
            .setSource(this, R.raw.pepperoni_pizza)
            .build()
            .thenAccept { renderable -> pizzaRenderable = renderable }
            .exceptionally { t: Throwable? ->
                var toast = Toast.makeText(
                    this, "Unable to load pizza renderable",
                    Toast.LENGTH_LONG
                )
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                null
            }

        ModelRenderable.builder()
            .setSource(this, R.raw.notebook)
            .build()
            .thenAccept { renderable -> bookRenderable = renderable }
            .exceptionally { t: Throwable? ->
                var toast = Toast.makeText(
                    this, "Unable to load book renderable",
                    Toast.LENGTH_LONG
                )
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                null
            }
    }

    //function set to be the onclick for the Available Coupons button
    fun showAvailCoupons(view: View) {
        // TODO: make coupon list visible here
    }

    //function set to be the onclick for the My Coupons button
    fun showMyCoupons(view: View) {
        // TODO: make coupon list visible here
    }

    private fun doLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    getAddress()
                }
            }
        }
    }

    //this is only looking at the piggy coupon!! && only for debug purposes rn
    private fun debugLocation(location: Location) {
//        deltaD.text = "Delta D: " + location.distanceTo(piggy.hardCodedLocation).toString()
//        curLatLon.text = "Cur Loc: " + location.latitude + ",\t" + location.longitude
//        goalLatLon.text =
//            "Goal Loc: " + piggy.hardCodedLocation.latitude + ",\t" + piggy.hardCodedLocation.longitude
//        can_set_model.text = "Can Set: " + canSetModel.toString()
    }

    //this is only checking the distance to piggy rn!!!!!*****
    private fun checkIsInThreshold(location: Location) {
        canSetModel = location.distanceTo(piggy.hardCodedLocation) <= distanceThreshold
            if (canSetModel) {
            in_thresh_button.setBackgroundColor(Color.GREEN)
            in_thresh_button.text = "Tap Screen to Display Nearest Coupon"
        } else {
            in_thresh_button.setBackgroundColor(Color.RED)
            in_thresh_button.text = "No Coupons in Range"
        }
    }

    private fun doSetOnTapArPlaneListener() {
        arFragment.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane, motionEvent: MotionEvent ->
            if (canSetModel) {
                val anchor = hitResult.createAnchor()
                val anchorNode = AnchorNode(anchor)
                anchorNode.setParent(arFragment.arSceneView.scene)

                val piggy = TransformableNode(arFragment.transformationSystem)
                piggy.setParent(anchorNode)
                piggy.renderable = piggyRenderable
                piggy.select()

                val pizza = TransformableNode(arFragment.transformationSystem)
                pizza.setParent(anchorNode)
                pizza.renderable = pizzaRenderable
                pizza.select()

                val book = TransformableNode(arFragment.transformationSystem)
                book.setParent(anchorNode)
                book.renderable = bookRenderable
                book.select()
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
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        requestingLocationUpdates = false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION),
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
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this, OnSuccessListener { location ->
            createLocationRequest()
            startLocationUpdates()
            if (location != null) {
                debugLocation(location)
                checkIsInThreshold(location)
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
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        requestingLocationUpdates = true
    }

    private fun hasLocationPermissions() = hasFineLocationPermission() && hasCoarseLocationPermissions()
    private fun hasFineLocationPermission() = ActivityCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    private fun hasCoarseLocationPermissions() = ActivityCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
}
