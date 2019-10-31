package com.example.cacherun

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.drawable.Drawable

class PointerDrawable: Drawable() {

    private var paint: Paint = Paint().apply { setARGB(255, 255, 0, 0)  }
    public var enabled: Boolean = false

    override fun draw(canvas: Canvas) {
        val cx: Float = canvas.width/2 + 0.0F
        val cy: Float = canvas.height/2 + 0.0F

        if (enabled) {
            paint.setColor(Color.GREEN)
            canvas.drawCircle(cx, cy, 10F, paint)
        } else {
            paint.setColor(Color.GRAY)
            canvas.drawText("X", cx, cy, paint)
        }

//            .width/2
    }














    override fun setAlpha(alpha: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getOpacity(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }













}