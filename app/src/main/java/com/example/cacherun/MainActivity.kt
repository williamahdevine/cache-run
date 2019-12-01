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
import android.widget.Button
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
import kotlin.math.round


class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var piggyRenderable: ModelRenderable
    private lateinit var pizzaRenderable: ModelRenderable
    private lateinit var bookRenderable: ModelRenderable
    private var availableCouponList: MutableList<Coupon> = arrayListOf()
    private var collectedCouponList: MutableList<Coupon> = arrayListOf()
    private var isShowingAvailOrCollectedCoupons = true

    private val piggyCoupon= Coupon("General Store", R.drawable.piggypng)
    private val pizzaCoupon= Coupon("Pizza Place", R.drawable.pizzapng)
    private val bookCoupon= Coupon("Book Store", R.drawable.bookpng)

    private lateinit var arFragment: ArFragment

    private var requestingLocationUpdates = false
    private var canSetModel = false

    // Set this to a high number if you want to enable placing of "coupons"
    // Set this to a low number if you want to disable placing of "coupons"
    private var distanceThreshold = 50.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        arFragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        doLocationCallback()
        makeRenderables()
        buildCoupons()
        doSetOnTapArPlaneListener()
        showAvailCoupons(this.recyclerView)
    }

    //builds all coupons and places them appropriately
    private fun buildCoupons() {
        piggyCoupon.hardCodedLocation.latitude= 44.673524
        piggyCoupon.hardCodedLocation.longitude = -63.614440
        availableCouponList.add(piggyCoupon)

        pizzaCoupon.hardCodedLocation.latitude= 44.673524
        pizzaCoupon.hardCodedLocation.longitude = -63.614440
        availableCouponList.add(pizzaCoupon)

        bookCoupon.hardCodedLocation.latitude= 44.673524
        bookCoupon.hardCodedLocation.longitude = -63.614440
        availableCouponList.add(bookCoupon)
    }

    //takes all sfb files and builds renderable models
    private fun makeRenderables(){
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
        val couponList: ArrayList<Coupon> = ArrayList()
        isShowingAvailOrCollectedCoupons = true
        findViewById<Button>(R.id.avail_coupons).setBackgroundColor(Color.GREEN)
        findViewById<Button>(R.id.my_coupons).setBackgroundColor(Color.GRAY)

        for (coupon in availableCouponList) {
            if (coupon.deltaD <= distanceThreshold){
                couponList.add(coupon)
            } else {
                couponList.remove(coupon)
            }
        }

        recyclerView.adapter = CouponAdapter(couponList)
    }

    //function set to be the onclick for the My Coupons button
    fun showCollectedCoupons(view: View) {
        isShowingAvailOrCollectedCoupons = false
        findViewById<Button>(R.id.avail_coupons).setBackgroundColor(Color.GRAY)
        findViewById<Button>(R.id.my_coupons).setBackgroundColor(Color.GREEN)
        val posts: ArrayList<Coupon> = ArrayList()

        for (coupon in collectedCouponList) {
            if (coupon.isCollected) {
                posts.add(coupon)
            }
        }

        recyclerView.adapter = CouponAdapter(posts)
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

    private fun checkIsInThreshold(location: Location) {
        var deltaD: Float

        for (coupon in availableCouponList) {
            deltaD = round(location.distanceTo(coupon.hardCodedLocation))
            coupon.deltaD = deltaD
            canSetModel = deltaD <= distanceThreshold
        }
    }

    private fun doSetOnTapArPlaneListener() {
        arFragment.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane, motionEvent: MotionEvent ->

            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)

            for (coupon in availableCouponList) {
                val transformableNode = TransformableNode(arFragment.transformationSystem)
                transformableNode.setParent(anchorNode)

                if (coupon.isSelected) {
                    transformableNode.renderable = when (coupon.name) {
                        "General Store" -> piggyRenderable
                        "Book Store" -> bookRenderable
                        "Pizza Place" -> pizzaRenderable
                        else -> null
                    }
                }

                transformableNode.select()
                coupon.isDisplayed = true
                transformableNode.setOnTouchListener { hitTestResult, motionEvent ->
                    anchorNode.removeChild(transformableNode)
                    collectCoupon(coupon)
                    true
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
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        requestingLocationUpdates = false
    }

    private fun getAddress() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this, OnSuccessListener { location ->
            createLocationRequest()
            startLocationUpdates()
            if (location != null) {
                checkIsInThreshold(location)
                if (isShowingAvailOrCollectedCoupons) {
                    showAvailCoupons(this.recyclerView) // hack to update distance to coupon
                }
            }
        }).addOnFailureListener(this) { e -> Log.w("getLastLocationFailure: onFailure", e)}
    }

    private fun collectCoupon(coupon: Coupon) {
        coupon.isDisplayed = false
        coupon.isCollected = true
        availableCouponList.remove(coupon)
        collectedCouponList.add(coupon)
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode != REQUEST_PERMISSIONS_REQUEST_CODE) return

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            getAddress()
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    private fun hasLocationPermissions() = hasFineLocationPermission() && hasCoarseLocationPermissions()
    private fun hasFineLocationPermission() = ActivityCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    private fun hasCoarseLocationPermissions() = ActivityCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
}
