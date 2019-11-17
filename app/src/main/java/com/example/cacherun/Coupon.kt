package com.example.cacherun

import android.location.Location
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.rendering.ModelRenderable


data class Coupon( val name: String) {
     //set name to file name like this below
     // @SerializedName("image_url")
     lateinit var Renderable: ModelRenderable
     var hardCodedLocation = Location("")
     var isCollected: Boolean? = null
     var isDisplayed: Boolean? = null
}

