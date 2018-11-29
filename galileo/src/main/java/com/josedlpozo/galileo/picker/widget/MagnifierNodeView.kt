/*
 * Copyright (C) 2016 The CyanogenMod Project
 *
 * Modified Work: Copyright (c) 2018 fr4nk1
 *
 * Modified Work: Copyright (c) 2018 josedlpozo
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
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import com.josedlpozo.galileo.R

class MagnifierNodeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val reticlePaint: Paint
    private val outlinePaint: Paint
    private val fillPaint: Paint
    private val clearPaint: Paint

    private var centerX: Float = 0.toFloat()
    private var centerY: Float = 0.toFloat()
    private var radius: Float = 0.toFloat()
    private val reticleRadius: Float
    private val density: Float

    init {
        val dm = resources.displayMetrics
        val twoDp = 2f * dm.density
        reticlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        reticlePaint.color = 0x50ffffff
        reticlePaint.strokeWidth = twoDp
        reticlePaint.style = Paint.Style.STROKE

        outlinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        outlinePaint.color = -0x7f000001
        outlinePaint.strokeWidth = twoDp
        outlinePaint.style = Paint.Style.STROKE
        outlinePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)

        fillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        fillPaint.color = -0x80000000
        fillPaint.strokeWidth = twoDp
        fillPaint.style = Paint.Style.FILL_AND_STROKE
        fillPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DARKEN)

        clearPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        clearPaint.color = 0
        clearPaint.strokeWidth = twoDp
        clearPaint.style = Paint.Style.FILL
        clearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        reticleRadius = resources.getInteger(R.integer.galileo_color_picker_sample_width) / 2 + twoDp
        density = dm.density
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = Math.min(w, h) / 2.0f - density * 2f
        centerX = w / 2.0f
        centerY = h / 2.0f
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(centerX, centerY, radius, fillPaint)
        canvas.drawCircle(centerX, centerY, radius, outlinePaint)
        canvas.drawCircle(centerX, centerY, reticleRadius, clearPaint)
        canvas.drawCircle(centerX, centerY, reticleRadius, reticlePaint)
    }
}
