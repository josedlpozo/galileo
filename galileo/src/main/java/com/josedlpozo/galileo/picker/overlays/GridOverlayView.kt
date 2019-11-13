package com.josedlpozo.galileo.picker.overlays

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.appcompat.content.res.AppCompatResources
import android.view.View
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.picker.utils.ColorUtils
import com.josedlpozo.galileo.picker.utils.PreferenceUtils

internal class GridOverlayView(context: Context) : View(context) {

    private lateinit var gridPaint: Paint
    private lateinit var keyLinePaint: Paint
    private lateinit var firstKeyLineRect: RectF
    private lateinit var secondKeyLineRect: RectF
    private lateinit var thirdKeyLineRect: RectF
    private var horizontalGridMarkerLeft: Drawable
    private var horizontalMarkerLeft: Drawable
    private var horizontalMarkerRight: Drawable
    private var verticalMarker: Drawable
    private lateinit var verticalGridMarkerBounds: Rect
    private lateinit var horizontalGridMarkerLeftBounds: Rect
    private lateinit var horizontalGridMarkerRightBounds: Rect
    private lateinit var firstKeyLineMarkerBounds: Rect
    private lateinit var secondKeyLineMarkerBounds: Rect
    private lateinit var thirdKeyLineMarkerBounds: Rect
    private var showGrid = false
    private var showKeyLines = false
    private val gridLineWidth: Float
    private var columnSize: Float = 0.toFloat()
    private var rowSize: Float = 0.toFloat()
    private val density: Float = resources.displayMetrics.density
    private val keyLineWidth: Float

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
        if (PreferenceUtils.GridPreferences.KEY_SHOW_GRID == key) {
            val enabled = prefs.getBoolean(PreferenceUtils.GridPreferences.KEY_SHOW_GRID, false)
            if (showGrid != enabled) {
                showGrid = enabled
                invalidate()
            }
        } else if (PreferenceUtils.GridPreferences.KEY_SHOW_KEYLINES == key) {
            val enabled = prefs.getBoolean(PreferenceUtils.GridPreferences.KEY_SHOW_KEYLINES, false)
            if (enabled != showKeyLines) {
                showKeyLines = enabled
                invalidate()
            }
        } else if (PreferenceUtils.GridPreferences.KEY_GRID_COLUMN_SIZE == key) {
            columnSize = density * PreferenceUtils.GridPreferences.getGridColumnSize(getContext(), resources.getInteger(
                    R.integer.galileo_default_column_size))
            invalidate()
        } else if (PreferenceUtils.GridPreferences.KEY_GRID_ROW_SIZE == key) {
            rowSize = density * PreferenceUtils.GridPreferences.getGridRowSize(getContext(),
                    resources.getInteger(R.integer.galileo_default_row_size))
            invalidate()
        } else if (PreferenceUtils.GridPreferences.KEY_GRID_LINE_COLOR == key) {
            gridPaint.color = ColorUtils.getGridLineColor(getContext())
            invalidate()
        } else if (PreferenceUtils.GridPreferences.KEY_KEYLINE_COLOR == key) {
            keyLinePaint.color = ColorUtils.getKeylineColor(getContext())
            invalidate()
        } else if (PreferenceUtils.GridPreferences.KEY_USE_CUSTOM_GRID_SIZE == key) {
            val useCustom = PreferenceUtils.GridPreferences.getUseCustomGridSize(getContext(), false)
            val defColumnSize = resources.getInteger(R.integer.galileo_default_column_size)
            val defRowSize = resources.getInteger(R.integer.galileo_default_row_size)
            columnSize = density * if (!useCustom)
                defColumnSize
            else
                PreferenceUtils.GridPreferences.getGridColumnSize(getContext(), defColumnSize)
            rowSize = density * if (!useCustom) defRowSize else PreferenceUtils.GridPreferences.getGridRowSize(getContext(), defRowSize)
            invalidate()
        }
    }

    init {
        gridLineWidth = density
        gridPaint = Paint()
        gridPaint.color = ColorUtils.getGridLineColor(context)
        gridPaint.strokeWidth = gridLineWidth
        keyLinePaint = Paint()
        keyLinePaint.color = ColorUtils.getKeylineColor(context)


        horizontalGridMarkerLeft = AppCompatResources.getDrawable(context, R.drawable.ic_marker_horiz_left)!!.mutate()
        horizontalMarkerLeft = AppCompatResources.getDrawable(context, R.drawable.ic_marker_horiz_left)!!
        horizontalMarkerRight = AppCompatResources.getDrawable(context, R.drawable.ic_marker_horiz_right)!!
        verticalMarker = AppCompatResources.getDrawable(context, R.drawable.ic_marker_vert)!!

        showGrid = PreferenceUtils.GridPreferences.getShowGrid(context, false)
        showKeyLines = PreferenceUtils.GridPreferences.getShowKeylines(context, false)

        val useCustom = PreferenceUtils.GridPreferences.getUseCustomGridSize(getContext(), false)
        val defColumnSize = resources.getInteger(R.integer.galileo_default_column_size)
        val defRowSize = resources.getInteger(R.integer.galileo_default_row_size)
        columnSize = density * if (!useCustom) defColumnSize else PreferenceUtils.GridPreferences.getGridColumnSize(getContext(), defColumnSize)
        rowSize = density * if (!useCustom) defRowSize else PreferenceUtils.GridPreferences.getGridRowSize(getContext(), defRowSize)
        keyLineWidth = 1.5f * density
    }

    override fun onDraw(canvas: Canvas) {
        drawGridLines(canvas)
        if (showKeyLines) {
            drawKeylines(canvas)
        }

        drawGridMarkers(canvas)
        if (showKeyLines) {
            drawKeylineMarkers(canvas)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val dm = resources.displayMetrics

        var width = (10 * dm.density).toInt()
        var height = (6 * dm.density).toInt()
        var x = (24 * dm.density).toInt()
        var y = 0
        verticalGridMarkerBounds = Rect(x, y, x + width, y + height)
        val temp = height
        height = width
        width = temp
        x = 0
        y = (8 * dm.density).toInt()
        horizontalGridMarkerLeftBounds = Rect(x, y, x + width, y + height)
        x = dm.widthPixels - (width - 1)
        horizontalGridMarkerRightBounds = Rect(x, y, x + width, y + height)

        x = (16 * dm.density).toInt()
        firstKeyLineMarkerBounds = Rect(x, y, x + width, y + height)
        x = (72 * dm.density).toInt()
        secondKeyLineMarkerBounds = Rect(x, y, x + width, y + height)
        x = dm.widthPixels - (16 * dm.density).toInt()
        thirdKeyLineMarkerBounds = Rect(x, y, x + width, y + height)

        firstKeyLineRect = RectF(0f, 0f, 16 * dm.density, dm.heightPixels.toFloat())
        secondKeyLineRect = RectF(56 * dm.density, 0f, 72 * dm.density, dm.heightPixels.toFloat())
        thirdKeyLineRect = RectF(dm.widthPixels - 16 * dm.density, 0f, dm.widthPixels.toFloat(), dm.heightPixels.toFloat())
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val prefs = PreferenceUtils.getShardedPreferences(context)
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        val prefs = PreferenceUtils.getShardedPreferences(context)
        prefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    private fun drawGridLines(canvas: Canvas) {
        val width = width
        val height = height

        var x = 0f
        while (x < width) {
            canvas.drawLine(x, 0f, x, (height - 1).toFloat(), gridPaint)
            x += columnSize
        }
        var y = 0f
        while (y < height) {
            canvas.drawLine(0f, y, (width - 1).toFloat(), y, gridPaint)
            y += rowSize
        }
    }

    private fun drawGridMarkers(canvas: Canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            verticalMarker.setTint(gridPaint.color)
            horizontalGridMarkerLeft.setTint(gridPaint.color)
            horizontalMarkerRight.setTint(gridPaint.color)
        }
        verticalMarker.bounds = verticalGridMarkerBounds
        verticalMarker.draw(canvas)
        horizontalGridMarkerLeft.bounds = horizontalGridMarkerLeftBounds
        horizontalGridMarkerLeft.draw(canvas)
        horizontalMarkerRight.bounds = horizontalGridMarkerRightBounds
        horizontalMarkerRight.draw(canvas)
    }

    private fun drawKeylines(canvas: Canvas) {
        val height = height

        val alpha = keyLinePaint.alpha
        // draw rects first
        keyLinePaint.alpha = (0.5f * alpha).toInt()
        canvas.drawRect(firstKeyLineRect, keyLinePaint)
        canvas.drawRect(secondKeyLineRect, keyLinePaint)
        canvas.drawRect(thirdKeyLineRect, keyLinePaint)

        // draw lines next
        keyLinePaint.alpha = alpha
        val stroke = keyLinePaint.strokeWidth
        keyLinePaint.strokeWidth = keyLineWidth
        canvas.drawLine(firstKeyLineRect.right, 0f, firstKeyLineRect.right, height.toFloat(), keyLinePaint)
        canvas.drawLine(secondKeyLineRect.right, 0f, secondKeyLineRect.right, height.toFloat(), keyLinePaint)
        canvas.drawLine(thirdKeyLineRect.left, 0f, thirdKeyLineRect.left, height.toFloat(), keyLinePaint)
        keyLinePaint.strokeWidth = stroke
    }

    private fun drawKeylineMarkers(canvas: Canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            horizontalMarkerLeft.setTint(keyLinePaint.color)
        }
        horizontalMarkerLeft.bounds = firstKeyLineMarkerBounds
        horizontalMarkerLeft.draw(canvas)
        horizontalMarkerLeft.bounds = secondKeyLineMarkerBounds
        horizontalMarkerLeft.draw(canvas)
        horizontalMarkerLeft.bounds = thirdKeyLineMarkerBounds
        horizontalMarkerLeft.draw(canvas)
    }
}