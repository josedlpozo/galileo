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

import android.content.Context
import com.josedlpozo.galileo.config.GalileoConfig
import com.josedlpozo.galileo.config.GalileoConfigBuilder
import com.josedlpozo.galileo.config.GalileoPlugin
import com.josedlpozo.galileo.items.emptyGalileoItem
import okhttp3.Interceptor

class Galileo(private val context: Context, config: GalileoConfig = GalileoConfigBuilder().build()) {

    fun start() {}

    fun stop() {}

    companion object {
        val interceptor : Interceptor = Interceptor { it.proceed(it.request()) }

        val preferator: GalileoPlugin = { emptyGalileoItem }

        val lynx: GalileoPlugin = { emptyGalileoItem }

        val chuck: GalileoPlugin = { emptyGalileoItem }

        val flow: GalileoPlugin = { emptyGalileoItem }

        val realm: GalileoPlugin = { emptyGalileoItem }

        val colorPicker: GalileoPlugin = { emptyGalileoItem }

        val grid: GalileoPlugin = { emptyGalileoItem }
    }
}