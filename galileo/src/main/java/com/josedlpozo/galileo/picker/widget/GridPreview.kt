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
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.josedlpozo.galileo.R

class GridPreview @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var columnSize: Float = 0.toFloat()
    private var rowSize: Float = 0.toFloat()
    private val density: Float = resources.displayMetrics.density
    private var columnSizeDp: Int = 0
    private var rowSizeDp: Int = 0

    private val gridLinePaint: Paint
    private val gridSizeTextPaint: Paint

    init {
        val gridLineWidth = DEFAULT_LINE_WIDTH * density
        columnSizeDp = DEFAULT_COLUMN_SIZE
        columnSize = columnSizeDp * density
        rowSizeDp = DEFAULT_ROW_SIZE
        rowSize = rowSizeDp * density

        gridLinePaint = Paint()
        gridLinePaint.color = ContextCompat.getColor(context, R.color.galileo_colorGridOverlayCardTint)
        gridLinePaint.strokeWidth = gridLineWidth

        gridSizeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        val textSize = resources.getDimensionPixelSize(R.dimen.galileo_grid_preview_text_size)
        gridSizeTextPaint.textSize = textSize.toFloat()
        gridSizeTextPaint.color = BACKGROUND_COLOR
    }

    override fun onDraw(canvas: Canvas) {
        val width = width.toFloat()
        val height = height.toFloat()

        canvas.drawColor(BACKGROUND_COLOR)
        var x = columnSize
        while (x < width) {
            canvas.drawLine(x, 0f, x, height, gridLinePaint)
            x += columnSize
        }
        var y = rowSize
        while (y < height) {
            canvas.drawLine(0f, y, width, y, gridLinePaint)
            y += rowSize
        }

        val text = String.format("%d x %d", columnSizeDp, rowSizeDp)
        val bounds = Rect()
        gridSizeTextPaint.getTextBounds(text, 0, text.length, bounds)
        canvas.drawText(text, (width - bounds.width()) / 2f, (height + bounds.height()) / 2f, gridSizeTextPaint)
    }

    fun setColumnSize(columnSize: Int) {
        columnSizeDp = columnSize
        this.columnSize = columnSizeDp * density
        invalidate()
    }

    fun getColumnSize(): Int {
        return columnSizeDp
    }

    fun setRowSize(rowSize: Int) {
        rowSizeDp = rowSize
        this.rowSize = rowSizeDp * density
        invalidate()
    }

    fun getRowSize(): Int {
        return rowSizeDp
    }

    companion object {
        private const val DEFAULT_LINE_WIDTH = 1f
        private const val DEFAULT_COLUMN_SIZE = 8
        private const val DEFAULT_ROW_SIZE = 8
        private const val BACKGROUND_COLOR = 0x1f000000
    }
}