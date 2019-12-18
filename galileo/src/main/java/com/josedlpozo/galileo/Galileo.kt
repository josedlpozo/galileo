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
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.hardware.SensorManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.josedlpozo.galileo.common.GalileoApplicationLifeCycle
import com.josedlpozo.galileo.config.GalileoConfig
import com.josedlpozo.galileo.config.GalileoConfigBuilder
import com.josedlpozo.galileo.config.GalileoOpenType
import com.josedlpozo.galileo.core.GalileoPlugin
import com.josedlpozo.galileo.floaticon.GalileoFloat
import com.josedlpozo.galileo.parent.home.HomeActivity
import com.josedlpozo.galileo.parent.preparator.PluginsPreparator
import com.josedlpozo.galileo.picker.GridGalileoItem
import com.josedlpozo.galileo.picker.overlays.GridOverlay
import com.squareup.seismic.ShakeDetector

class Galileo(
    private val application: Application,
    private val config: GalileoConfig = GalileoConfigBuilder().defaultPlugins().build()
) : LifecycleObserver {

    private val galileoFloat = GalileoFloat {
        Intent(application, HomeActivity::class.java).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
        }.also {
            application.startActivity(it)
        }
    }
    private val shakeDetector: ShakeDetector = ShakeDetector {
        Intent(application, HomeActivity::class.java).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
        }.also {
            application.startActivity(it)
        }
    }

    init {
        preparePlugins()
        when (config.openType) {
            GalileoOpenType.Floating -> initFloatingViews()
            GalileoOpenType.Shaking -> {
                ProcessLifecycleOwner.get().lifecycle.addObserver(this)
                start()
            }
            GalileoOpenType.Both -> {
                ProcessLifecycleOwner.get().lifecycle.addObserver(this)
                start()
                initFloatingViews()
            }
        }
    }

    private val sensorManager: SensorManager
        get() = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        when (config.openType) {
            GalileoOpenType.Shaking, GalileoOpenType.Both -> {
                start()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        when (config.openType) {
            GalileoOpenType.Shaking, GalileoOpenType.Both -> {
                stop()
            }
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

    private fun initFloatingViews() {
        val floats = listOf(galileoFloat, GridOverlay())
        application.registerActivityLifecycleCallbacks(GalileoApplicationLifeCycle(floats))
    }

    companion object {
        val grid: GalileoPlugin = { GridGalileoItem(it) }
    }
}