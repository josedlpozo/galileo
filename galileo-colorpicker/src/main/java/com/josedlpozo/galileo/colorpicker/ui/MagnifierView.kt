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
package com.josedlpozo.galileo.colorpicker.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.josedlpozo.galileo.colorpicker.R


class MagnifierView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(
    context,
    attrs,
    defStyleAttr
) {

    companion object {
        private const val PREFIX_COLOR = "galileo_"
    }

    private lateinit var colorValueTextView: TextView
    private lateinit var colorNameTextView: TextView

    private val magnifyingLens: Drawable? = ContextCompat.getDrawable(context, R.drawable.loop_ring)
    private var pixels: Bitmap? = null
    private val bitmapPaint: Paint = Paint()
    private val gridPaint: Paint
    private val pixelOutlinePaint: Paint

    private var sourcePreviewRect: Rect? = null
    private val destinationPreviewRect: Rect
    private var targetPixelOutline: RectF? = null

    private val insets: Point
    private val previewClipPath: Path

    private var centerPixelColor: Int = 0
    private val colors = mutableMapOf<String, String>()

    init {
        bitmapPaint.isAntiAlias = false
        bitmapPaint.isDither = true
        bitmapPaint.isFilterBitmap = false

        val dm = resources.displayMetrics
        gridPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        gridPaint.color = -0x1000000
        gridPaint.alpha = 128
        gridPaint.style = Paint.Style.STROKE
        gridPaint.strokeWidth = 1f * dm.density
        gridPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DARKEN)

        pixelOutlinePaint = Paint()
        pixelOutlinePaint.color = -0x1000000
        pixelOutlinePaint.style = Paint.Style.STROKE
        pixelOutlinePaint.strokeWidth = 2f * dm.density
        pixelOutlinePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DARKEN)

        val res = resources
        insets = Point(
            res.getDimensionPixelSize(R.dimen.galileo_magnified_image_horizontal_inset),
            res.getDimensionPixelSize(R.dimen.galileo_magnified_image_vertical_inset)
        )

        val previewSize = res.getDimensionPixelSize(R.dimen.galileo_magnified_image_size)
        destinationPreviewRect =
            Rect(insets.x, insets.y, insets.x + previewSize, insets.y + previewSize)
        previewClipPath = Path()
        previewClipPath.addCircle(
            destinationPreviewRect.exactCenterX(),
            destinationPreviewRect.exactCenterY(), previewSize / 2f, Path.Direction.CCW
        )

        try {
            val fields =
                Class.forName(context.applicationContext.javaClass.`package`?.name + ".R\$color")
                    .declaredFields.filter { it.name.startsWith(PREFIX_COLOR) }
            for (field in fields) {
                val colorName = field.name.replace(PREFIX_COLOR, "")
                val colorId = field.getInt(null)
                val colorHex =
                    String.format("#%06X", ContextCompat.getColor(context, colorId) and 0x00ffffff)
                colors[colorHex] = colorName
            }
        } catch (e: Exception) {
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        colorValueTextView = findViewById(R.id.color_value)
        colorNameTextView = findViewById(R.id.color_name)
        colorValueTextView.setOnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@setOnClickListener
            val cm = context.getSystemService(ClipboardManager::class.java)
            val text = colorValueTextView.text
            val primaryClip = cm!!.primaryClip
            if (primaryClip?.getItemAt(0) == null || text != cm.primaryClip!!.getItemAt(0).coerceToText(
                    context
                )
            ) {
                val clip = ClipData.newPlainText("color", text)
                cm.setPrimaryClip(clip)
                Toast.makeText(context, R.string.color_copied_to_clipboard, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        magnifyingLens!!.setBounds(0, 0, w, h)
    }

    override fun dispatchDraw(canvas: Canvas) {
        magnifyingLens!!.draw(canvas)
        if (pixels != null) {
            canvas.clipPath(previewClipPath)
            canvas.drawBitmap(pixels!!, sourcePreviewRect, destinationPreviewRect, bitmapPaint)
            drawGrid(canvas)
            canvas.drawRect(targetPixelOutline!!, pixelOutlinePaint)
        }
        super.dispatchDraw(canvas)
    }

    private fun drawGrid(canvas: Canvas) {
        val stepSize = destinationPreviewRect.width() / sourcePreviewRect!!.width().toFloat()

        var x = destinationPreviewRect.left + stepSize
        while (x <= destinationPreviewRect.right) {
            canvas.drawLine(
                x,
                destinationPreviewRect.top.toFloat(),
                x,
                destinationPreviewRect.bottom.toFloat(),
                gridPaint
            )
            x += stepSize
        }
        var y = destinationPreviewRect.top + stepSize
        while (y <= destinationPreviewRect.bottom) {
            canvas.drawLine(
                destinationPreviewRect.left.toFloat(),
                y,
                destinationPreviewRect.right.toFloat(),
                y,
                gridPaint
            )
            y += stepSize
        }
    }

    fun setPixels(pixels: Bitmap) {
        this.pixels = pixels
        sourcePreviewRect = Rect(0, 0, pixels.width, pixels.height)
        centerPixelColor = pixels.getPixel(pixels.width / 2, pixels.height / 2)

        if (targetPixelOutline == null) {
            val pixelSize = destinationPreviewRect.width().toFloat() / pixels.width
            val x = (pixels.width - 1) / 2f * pixelSize
            val y = (pixels.height - 1) / 2f * pixelSize
            targetPixelOutline = RectF(
                destinationPreviewRect.left + x,
                destinationPreviewRect.top + y,
                destinationPreviewRect.left.toFloat() + x + pixelSize,
                destinationPreviewRect.top.toFloat() + y + pixelSize
            )
        }

        val format = String.format("#%06X", centerPixelColor and 0x00ffffff)
        val match = colors[format] ?: ""
        colorValueTextView.text = format
        colorNameTextView.text = match
        colorNameTextView.visibility = View.VISIBLE
        colorValueTextView.visibility = View.VISIBLE
        invalidate()
    }
}