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
import com.josedlpozo.galileo.chuck.GalileoChuckInterceptor
import com.josedlpozo.galileo.chuck.internal.ui.TransactionListView
import com.josedlpozo.galileo.config.ConfigRepository
import com.josedlpozo.galileo.config.GalileoConfig
import com.josedlpozo.galileo.config.GalileoConfigBuilder
import com.josedlpozo.galileo.config.GalileoInternalConfig
import com.josedlpozo.galileo.config.GalileoInternalPlugin
import com.josedlpozo.galileo.config.GalileoPlugin
import com.josedlpozo.galileo.flow.FlowEventTry
import com.josedlpozo.galileo.flow.FlowView
import com.josedlpozo.galileo.lynx.GalileoLynx
import com.josedlpozo.galileo.more.MoreView
import com.josedlpozo.galileo.parent.home.HomeActivity
import com.josedlpozo.galileo.preferator.Preferator
import com.josedlpozo.galileo.realm.RealmView
import com.squareup.seismic.ShakeDetector
import okhttp3.Interceptor

class Galileo(private val application: Application, private val config: GalileoConfig = GalileoConfigBuilder().defaultPlugins().build()) : LifecycleObserver {

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        application.registerActivityLifecycleCallbacks(FlowEventTry.flowLifeCycleCallback)
        preparePlugins()
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
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        stop()
    }

    private fun preparePlugins() {
        ConfigRepository.internalConfig = if (config.plugins.size > MAX_ITEMS) {
            val first = config.plugins.take(MAX_SIZE_LIST).map { GalileoInternalPlugin(System.nanoTime(), it) }
            val more = config.plugins.drop(MAX_SIZE_LIST).map { GalileoInternalPlugin(System.nanoTime(), it) }
            ConfigRepository.more = more
            GalileoInternalConfig(first + GalileoInternalPlugin(System.nanoTime()) { MoreView(more, it) })
        } else GalileoInternalConfig(config.plugins.map { GalileoInternalPlugin(System.nanoTime(), it) })
    }

    private fun start() {
        shakeDetector.start(sensorManager)
    }

    private fun stop() {
        shakeDetector.stop()
    }

    companion object {
        private const val MAX_ITEMS = 5
        private const val MAX_SIZE_LIST = MAX_ITEMS - 1

        val interceptor: Interceptor = GalileoChuckInterceptor

        val preferator: GalileoPlugin = { Preferator.view(it) }

        val lynx: GalileoPlugin = { GalileoLynx(it) }

        val chuck: GalileoPlugin = { TransactionListView(it) }

        val flow: GalileoPlugin = { FlowView(it) }

        val realm: GalileoPlugin = { RealmView(it) }
    }
}