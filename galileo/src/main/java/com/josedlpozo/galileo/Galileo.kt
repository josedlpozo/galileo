/*
 * Copyright (C) 2018 josedlpozo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import android.view.WindowManager
import com.josedlpozo.galileo.chuck.GalileoChuckInterceptor
import com.josedlpozo.galileo.chuck.ui.TransactionGalileoItem
import com.josedlpozo.galileo.common.FloatItem
import com.josedlpozo.galileo.common.GalileoFloat
import com.josedlpozo.galileo.common.GalileoFloatLifeCycle
import com.josedlpozo.galileo.common.Permission
import com.josedlpozo.galileo.config.GalileoConfig
import com.josedlpozo.galileo.config.GalileoConfigBuilder
import com.josedlpozo.galileo.config.GalileoPlugin
import com.josedlpozo.galileo.flow.FlowEventTry
import com.josedlpozo.galileo.flow.FlowGalileoItem
import com.josedlpozo.galileo.lynx.LynxGalileoItem
import com.josedlpozo.galileo.parent.home.HomeActivity
import com.josedlpozo.galileo.parent.preparator.PluginsPreparator
import com.josedlpozo.galileo.picker.GridGalileoItem
import com.josedlpozo.galileo.picker.PickerGalileoItem
import com.josedlpozo.galileo.picker.overlays.GridOverlay
import com.josedlpozo.galileo.preferator.view.PreferatorGalileoItem
import com.josedlpozo.galileo.realm.RealmGalileoItem
import com.squareup.seismic.ShakeDetector
import okhttp3.Interceptor

class Galileo(private val application: Application, private val config: GalileoConfig = GalileoConfigBuilder().defaultPlugins().build()) : LifecycleObserver {

    private val windowManager: WindowManager
    private val floats: List<FloatItem>
    private val galileoFloat = GalileoFloat {
        Intent(application, HomeActivity::class.java).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
        }.also {
            application.startActivity(it)
        }
    }

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        application.registerActivityLifecycleCallbacks(FlowEventTry.flowLifeCycleCallback)
        application.registerActivityLifecycleCallbacks(GalileoFloatLifeCycle(galileoFloat))

        preparePlugins()

        windowManager = application.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        floats = listOf(GridOverlay())

        if (!Permission.canDrawOverlays(application.applicationContext)) {
            Permission.requestDrawOverlays(application.applicationContext)
        }

        galileoFloat.performCreate(application.applicationContext)
        windowManager.addView(galileoFloat.rootView, galileoFloat.layoutParams)

        floats.map {
            it.performCreate(application.applicationContext)
            windowManager.addView(it.rootView, it.layoutParams)
        }
    }

    private val shakeDetector: ShakeDetector = ShakeDetector {
        Intent(application, HomeActivity::class.java).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
        }.also {
            application.startActivity(it)
        }
    }

    private val sensorManager: SensorManager
        get() = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        start()

        galileoFloat.show()
        floats.map {
            it.onEnterForeground()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        stop()

        galileoFloat.hide()
        floats.map {
            it.onEnterBackground()
        }
    }

    private fun preparePlugins() {
        PluginsPreparator.prepare(config)
    }

    private fun start() {
        shakeDetector.start(sensorManager)
    }

    private fun stop() {
        shakeDetector.stop()
    }

    companion object {

        val interceptor: Interceptor = GalileoChuckInterceptor

        val preferator: GalileoPlugin = { PreferatorGalileoItem(it) }

        val lynx: GalileoPlugin = { LynxGalileoItem(it) }

        val chuck: GalileoPlugin = { TransactionGalileoItem(it) }

        val flow: GalileoPlugin = { FlowGalileoItem(it) }

        val realm: GalileoPlugin = { RealmGalileoItem(it) }

        val colorPicker: GalileoPlugin = { PickerGalileoItem(it) }

        val grid: GalileoPlugin = { GridGalileoItem(it) }
    }
}