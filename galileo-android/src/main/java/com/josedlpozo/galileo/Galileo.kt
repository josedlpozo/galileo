package com.josedlpozo.galileo

import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import com.squareup.seismic.ShakeDetector

class Galileo(private val context: Context) {

    private val shakeDetector : ShakeDetector = ShakeDetector {
        Intent(context, HomeActivity::class.java).also {
            context.startActivity(it)
        }
    }

    private val sensorManager : SensorManager
        get() = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    fun start() {
        shakeDetector.start(sensorManager)
    }

    fun stop() {
        shakeDetector.stop()
    }
}