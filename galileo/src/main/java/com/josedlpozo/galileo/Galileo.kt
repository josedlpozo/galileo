package com.josedlpozo.galileo

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ProcessLifecycleOwner
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.hardware.SensorManager
import com.josedlpozo.galileo.parent.home.HomeActivity
import com.josedlpozo.galileo.chuck.GalileoChuckInterceptor
import com.josedlpozo.galileo.chuck.GalileoChuckInterceptorOld
import com.squareup.seismic.ShakeDetector
import okhttp3.Interceptor

class Galileo(private val application: Application) : LifecycleObserver{

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private val shakeDetector : ShakeDetector = ShakeDetector {
        Intent(application, HomeActivity::class.java).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
        }.also {
            application.startActivity(it)
        }
    }

    private val sensorManager : SensorManager
        get() = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        stop()
    }

    private fun start() {
        shakeDetector.start(sensorManager)
    }

    private fun stop() {
        shakeDetector.stop()
    }

    companion object {
        val interceptor : Interceptor = GalileoChuckInterceptor.getInstance() as Interceptor

        val interceptorOld: com.squareup.okhttp.Interceptor = GalileoChuckInterceptorOld.getInstance() as com.squareup.okhttp.Interceptor
    }
}