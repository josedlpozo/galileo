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
package com.josedlpozo.galileo.sample

import android.app.Application
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.TextView
import com.josedlpozo.galileo.Galileo
import com.josedlpozo.galileo.config.GalileoConfigBuilder
import com.josedlpozo.galileo.items.GalileoItem

class GalileoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Galileo(this, GalileoConfigBuilder().defaultPlugins().build())
    }

    class SamplePlugin(context: Context) : LinearLayout(context), GalileoItem {
        override val view: View = this
        override val name: String = "SamplePlugin"
        override val icon: Int = R.mipmap.ic_launcher_round
        override fun snapshot(): String = "Testing"

        init {
            addView(TextView(context).apply {
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                text = "This is a sample Galileo Plugin"
                gravity = Gravity.CENTER
            })
        }
    }
}