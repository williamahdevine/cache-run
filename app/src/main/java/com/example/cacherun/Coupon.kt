package com.example.cacherun

import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.rendering.ModelRenderable

class Coupon: AppCompatActivity() {
    private lateinit var piggyRenderable: ModelRenderable
    private lateinit var pizzaRenderable: ModelRenderable
    private lateinit var notebookRenderable: ModelRenderable
    private var distanceThreshold = 100000.0
    private lateinit var hardCodedLocation: Location
    private var isCollected = false
    private var isDisplayed = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //coupon locations
        hardCodedLocation = Location("")
        hardCodedLocation.latitude = 44.636
        hardCodedLocation.longitude = -63.591


    }

}