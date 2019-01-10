/*
 * Copyright (C) 2018 vicfran.
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
package com.josedlpozo.galileo.screenshooter

import android.app.Activity
import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.LinearLayout
import android.widget.Switch
import com.josedlpozo.galileo.items.GalileoItem
import com.josedlpozo.galileo.picker.ui.DesignerTools
import com.josedlpozo.galileo.R

internal class ScreenshooterView internal constructor(context: Context) : LinearLayout(context), OnCheckedChangeListener, GalileoItem {

    override val view: View = this
    override val name: String = "Screenshooter"
    override val icon: Int = R.drawable.ic_screenshooter
    override fun snapshot(): String = ""

    private lateinit var myGestureListener: MyGestureListener
    private lateinit var gestureDetector: GestureDetectorCompat

    private val swScreenshooter: Switch

    init {
        LayoutInflater.from(context).inflate(R.layout.view_screenshooter, this)
        orientation = HORIZONTAL
        swScreenshooter = findViewById(R.id.swScrenshooter)
        swScreenshooter.setOnCheckedChangeListener(this)
        swScreenshooter.isChecked = DesignerTools.colorPickerOn(context)
    }

    fun onActivityResumed(activity: Activity) {
        myGestureListener = MyGestureListener(context, Shooter(context, activity))
        gestureDetector = GestureDetectorCompat(context, myGestureListener)
        activity.window.decorView.findViewById<View>(android.R.id.content).setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        isFeatureEnabled = isChecked
    }

    private fun checkPermission(activity: Activity) {
        // TODO: now in second activity
    }

    companion object {
        private var isFeatureEnabled: Boolean = false
    }

    private class MyGestureListener(private val context: Context, private val shooter: Shooter) : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(event: MotionEvent): Boolean {
            return true
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            if (isFeatureEnabled) shooter.shoot()
            return true
        }
    }

}