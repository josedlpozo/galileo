package com.josedlpozo.galileo.picker.overlays

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.graphics.Point
import android.graphics.Rect
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Handler
import androidx.core.content.ContextCompat.getSystemService
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.common.ColorMapper
import com.josedlpozo.galileo.parent.extensions.hide
import com.josedlpozo.galileo.parent.extensions.show
import com.josedlpozo.galileo.picker.ui.DesignerTools
import com.josedlpozo.galileo.picker.utils.PreferenceUtils
import com.josedlpozo.galileo.picker.widget.MagnifierNodeView
import com.josedlpozo.galileo.picker.widget.MagnifierView
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@TargetApi(21)
internal class ColorPickerOverlayView(context: Context) : FrameLayout(context) {

    private lateinit var imageReader: ImageReader
    private var previewArea: Rect
    private var previewSampleHeight: Int
    private var previewSampleWidth: Int
    private var nodeToMagnifierDistance: Float
    private var angle: Float = Math.PI.toFloat() * 1.5f
    private var magnifierParams: LayoutParams
    private var nodeParams: LayoutParams
    private var magnifierNodeView: MagnifierNodeView = MagnifierNodeView(context)
    private var magnifierView: MagnifierView =
        inflate(context, R.layout.color_picker_magnifier, null) as MagnifierView
    private val screenCaptureLock = Any()
    private lateinit var mediaProjectionManager: MediaProjectionManager
    private lateinit var mediaProjection: MediaProjection
    private lateinit var virtualDisplay: VirtualDisplay

    private val mOnTouchListener = OnTouchListener { _, event ->
        when (event.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                magnifierNodeView.visibility = View.GONE
                val rawX = event.rawX
                val rawY = event.rawY
                val dx = magnifierView.x + magnifierView.width / 2 - rawX
                val dy = magnifierView.y + magnifierView.height / 2 - rawY
                angle = atan2(dy.toDouble(), dx.toDouble()).toFloat()
                updateMagnifierViewPosition(rawX.toInt(), rawY.toInt(), angle)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> magnifierNodeView.visibility =
                VISIBLE
        }
        true
    }

    private val preferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            if (PreferenceUtils.ColorPickerPreferences.KEY_COLOR_PICKER_ENABLED == key) {
                val enabled = prefs.getBoolean(
                    PreferenceUtils.ColorPickerPreferences.KEY_COLOR_PICKER_ENABLED,
                    false
                )
                if (enabled) {
                    show()
                    magnifierNodeView.show()
                    magnifierView.show()
                } else {
                    hide()
                    magnifierNodeView.hide()
                    magnifierView.hide()
                }
            }
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

    init {
        magnifierNodeView.setOnTouchListener(mOnTouchListener)

        addView(magnifierView)
        addView(magnifierNodeView)

        val res = resources

        val magnifierWidth = res.getDimensionPixelSize(R.dimen.galileo_picker_magnifying_ring_width)
        val magnifierHeight =
            res.getDimensionPixelSize(R.dimen.galileo_picker_magnifying_ring_height)

        val nodeViewSize = res.getDimensionPixelSize(R.dimen.galileo_picker_node_size)
        val dm = res.displayMetrics

        nodeParams = LayoutParams(nodeViewSize, nodeViewSize)
        magnifierParams = LayoutParams(magnifierWidth, magnifierHeight)

        val x = dm.widthPixels / 2
        val y = dm.heightPixels / 2
        magnifierNodeView.x = (x - nodeViewSize / 2).toFloat()
        magnifierNodeView.y = (y - nodeViewSize / 2).toFloat()

        magnifierView.x = (x - magnifierWidth / 2).toFloat()
        magnifierView.y = magnifierNodeView.y - (magnifierHeight + nodeViewSize / 2)

        magnifierNodeView.layoutParams = nodeParams
        magnifierView.layoutParams = magnifierParams

        previewSampleWidth = res.getInteger(R.integer.galileo_color_picker_sample_width)
        previewSampleHeight = res.getInteger(R.integer.galileo_color_picker_sample_height)
        previewArea = Rect(
            x - previewSampleWidth / 2,
            y - previewSampleHeight / 2,
            x + previewSampleWidth / 2 + 1,
            y + previewSampleHeight / 2 + 1
        )
        nodeToMagnifierDistance = (min(magnifierWidth, magnifierHeight) + nodeViewSize * 2) / 2f

    }

    fun setupMediaProjection() {
        if (DesignerTools.screenRecordResultData == null) return
        val dm = resources.displayMetrics
        val size = Point()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        windowManager?.defaultDisplay?.getRealSize(size)
        imageReader = ImageReader.newInstance(size.x, size.y, PixelFormat.RGBA_8888, 2)
        imageReader.setOnImageAvailableListener(mImageAvailableListener, Handler())
        mediaProjectionManager =
            context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        mediaProjection = mediaProjectionManager.getMediaProjection(
            DesignerTools.screenRecordResultCode,
            DesignerTools.screenRecordResultData!!
        )
        virtualDisplay = mediaProjection.createVirtualDisplay(
            ColorPickerOverlayView::class.java.simpleName, size.x, size.y, dm.densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, imageReader.surface, null, null
        )
        magnifierNodeView.show()
        magnifierView.show()
    }

    fun unregisterMediaProjection() {
        if (!::virtualDisplay.isInitialized || !::mediaProjection.isInitialized || !::imageReader.isInitialized) return
        virtualDisplay.release()
        mediaProjection.stop()

        imageReader.close()
    }

    private fun updateMagnifierViewPosition(x: Int, y: Int, angle: Float) {
        magnifierNodeView.x = x.toFloat() - magnifierNodeView.width / 2
        magnifierNodeView.y = y.toFloat() - statusBarHeight() - magnifierNodeView.height / 2

        previewArea.left = x - previewSampleWidth / 2
        previewArea.top = y - previewSampleHeight / 2
        previewArea.right = x + previewSampleWidth / 2 + 1
        previewArea.bottom = y + previewSampleHeight / 2 + 1

        magnifierView.x =
            ((nodeToMagnifierDistance * cos(angle.toDouble()).toFloat() + x).toInt() - magnifierView.width / 2).toFloat()
        magnifierView.y =
            ((nodeToMagnifierDistance * sin(angle.toDouble()).toFloat() + y).toInt() - magnifierView.height / 2).toFloat()
    }

    private val mImageAvailableListener = ImageReader.OnImageAvailableListener { reader ->
        synchronized(screenCaptureLock) {
            val newImage = reader.acquireNextImage()
            if (newImage != null) {
                magnifierView.setPixels(getScreenBitmapRegion(newImage, previewArea)!!)
                newImage.close()
            }
        }
    }

    private fun getScreenBitmapRegion(image: Image?, region: Rect): Bitmap? {
        if (image == null) {
            return null
        }
        val maxX = image.width - 1
        val maxY = image.height - 1
        val width = region.width()
        val height = region.height()
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val planes = image.planes
        val buffer = planes[0].buffer
        val rowStride = planes[0].rowStride
        val pixelStride = planes[0].pixelStride
        var color: Int
        var pixelX: Int
        var pixelY: Int
        for (y in 0 until height) {
            for (x in 0 until width) {
                pixelX = region.left + x
                pixelY = region.top + y
                color = if (pixelX in 0..maxX && pixelY >= 0 && pixelY <= maxY) {
                    val index = pixelY * rowStride + pixelX * pixelStride
                    buffer.position(index)
                    ColorMapper.color(buffer)
                } else {
                    0
                }
                bmp.setPixel(x, y, color)
            }
        }
        return bmp
    }

    private fun statusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

}