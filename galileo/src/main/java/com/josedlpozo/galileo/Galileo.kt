package com.josedlpozo.galileo

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.hardware.SensorManager
import com.josedlpozo.galileo.parent.home.HomeActivity
import com.josedlpozo.galileo.chuck.GalileoChuckInterceptor
import com.josedlpozo.galileo.chuck.GalileoChuckInterceptorOld
import com.squareup.seismic.ShakeDetector
import okhttp3.Interceptor

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

    companion object {
        val interceptor : Interceptor = GalileoChuckInterceptor.getInstance() as Interceptor

        val interceptorOld: com.squareup.okhttp.Interceptor = GalileoChuckInterceptorOld.getInstance() as com.squareup.okhttp.Interceptor
    }
}