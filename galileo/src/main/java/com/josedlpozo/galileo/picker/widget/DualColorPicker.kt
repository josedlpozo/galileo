/*
 * Copyright (C) 2016 The CyanogenMod Project
 *
 * Modified Work: Copyright (c) 2018 fr4nk1
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
package com.josedlpozo.galileo.picker.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Region
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.picker.utils.PreferenceUtils

class DualColorPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val primaryFillPaint: Paint
    private val secondaryFillPaint: Paint
    private val primaryStrokePaint: Paint
    private val secondaryStrokePaint: Paint

    var primaryColor: Int
        get() = primaryFillPaint.color
        set(color) {
            primaryFillPaint.color = color
            primaryStrokePaint.color = getDarkenedColor(color)
            invalidate()
        }

    var secondaryColor: Int
        get() = secondaryFillPaint.color
        set(color) {
            secondaryFillPaint.color = color
            secondaryStrokePaint.color = getDarkenedColor(color)
            invalidate()
        }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.DualColorPicker, 0, 0)
        val gridPreferences = PreferenceUtils.GridPreferences
        val defaultPrimaryColor = gridPreferences.getGridLineColor(context, ContextCompat.getColor(context, R.color.galileo_dualColorPickerDefaultPrimaryColor))
        val defaultSecondaryColor = gridPreferences.getKeylineColor(context, ContextCompat.getColor(context, R.color.galileo_dualColorPickerDefaultSecondaryColor))
        val primaryColor = ta.getColor(R.styleable.DualColorPicker_primaryColor, defaultPrimaryColor)
        val secondaryColor = ta.getColor(R.styleable.DualColorPicker_primaryColor, defaultSecondaryColor)

        primaryFillPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        primaryFillPaint.style = Paint.Style.FILL
        primaryFillPaint.color = primaryColor
        primaryStrokePaint = Paint(primaryFillPaint)
        primaryStrokePaint.style = Paint.Style.STROKE
        primaryStrokePaint.strokeWidth = STROKE_WIDTH
        primaryStrokePaint.color = getDarkenedColor(primaryColor)

        secondaryFillPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        secondaryFillPaint.style = Paint.Style.FILL_AND_STROKE
        secondaryFillPaint.color = secondaryColor
        secondaryStrokePaint = Paint(secondaryFillPaint)
        secondaryStrokePaint.style = Paint.Style.STROKE
        secondaryStrokePaint.strokeWidth = STROKE_WIDTH
        secondaryStrokePaint.color = getDarkenedColor(secondaryColor)

        ta.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        val width = width.toFloat()
        val height = height.toFloat()
        val widthDiv2 = width / 2f
        val heightDiv2 = height / 2f
        val radius = Math.min(widthDiv2, heightDiv2) * 0.9f

        // erase everything
        canvas.drawColor(0)

        // draw the left half
        canvas.clipRect(0f, 0f, widthDiv2, height, Region.Op.REPLACE)
        canvas.drawCircle(widthDiv2, heightDiv2, radius, primaryFillPaint)
        canvas.drawCircle(widthDiv2, heightDiv2, radius, primaryStrokePaint)
        canvas.drawLine(widthDiv2 - STROKE_WIDTH / 2f, heightDiv2 - radius,
                widthDiv2 - STROKE_WIDTH / 2f, heightDiv2 + radius, primaryStrokePaint)

        /// draw the right half
        canvas.clipRect(widthDiv2, 0f, width, height, Region.Op.REPLACE)
        canvas.drawCircle(widthDiv2, heightDiv2, radius, secondaryFillPaint)
        canvas.drawCircle(widthDiv2, heightDiv2, radius, secondaryStrokePaint)
        canvas.drawLine(widthDiv2 + STROKE_WIDTH / 2f, heightDiv2 - radius,
                widthDiv2 + STROKE_WIDTH / 2f, heightDiv2 + radius, secondaryStrokePaint)
    }

    private fun getDarkenedColor(color: Int): Int {
        val a = Color.alpha(color)
        val r = (Color.red(color) * COLOR_DARKEN_FACTOR).toInt()
        val g = (Color.green(color) * COLOR_DARKEN_FACTOR).toInt()
        val b = (Color.blue(color) * COLOR_DARKEN_FACTOR).toInt()

        return Color.argb(a, r, g, b)
    }

    companion object {
        private const val STROKE_WIDTH = 5f
        private const val COLOR_DARKEN_FACTOR = 0.8f
    }
}