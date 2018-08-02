package com.josedlpozo.galileo

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.hardware.SensorManager
import com.squareup.seismic.ShakeDetector

class Galileo(private val context: Context) {

    private val shakeDetector : ShakeDetector = ShakeDetector {
        Intent(context, HomeActivity::class.java).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
        }.also {
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