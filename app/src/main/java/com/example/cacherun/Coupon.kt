package com.example.cacherun

import android.location.Location
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.rendering.ModelRenderable


data class Coupon(var name: String, val imageId: Int) {
    //set name to file name like this below
    // @SerializedName("image_url")
    lateinit var Renderable: ModelRenderable
    var hardCodedLocation = Location("")
    var isCollected = false
    var isDisplayed = false
    var isInRange = false
    var deltaD = 0.0F
    var isSelected = false



}

