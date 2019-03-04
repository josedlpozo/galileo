package com.josedlpozo.galileo.picker.overlays

import android.app.Notification
import android.app.PendingIntent
import android.content.*
import android.graphics.PixelFormat
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.common.ServiceFloatItem
import com.josedlpozo.galileo.picker.qs.GridQuickSettingsTile
import com.josedlpozo.galileo.picker.qs.OnOffTileState
import com.josedlpozo.galileo.picker.ui.DesignerTools
import com.josedlpozo.galileo.picker.utils.PreferenceUtils

class GridOverlayService : ServiceFloatItem() {

    override val notificationId: Int
        get() = this.javaClass.hashCode()
    override val hasToNotify: Boolean
        get() = true

    private lateinit var windowManager: WindowManager

    override fun onViewCreated(view: View) {

    }

    override fun onCreateView(context: Context, view: ViewGroup?): View = GridOverlayView(context)

    override fun onLayoutParamsCreated(params: WindowManager.LayoutParams) {
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        params.format = PixelFormat.TRANSLUCENT
    }

    override fun onCreate(context: Context) {
        super.onCreate(context)
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        handleVisibility()
        val prefs = PreferenceUtils.getShardedPreferences(context)
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onEnterForeground() {
        super.onEnterForeground()
        handleVisibility()
    }

    override fun onEnterBackground() {
        super.onEnterBackground()
        handleVisibility()
    }

    override fun onDestroy() {
        super.onDestroy()
        val prefs = PreferenceUtils.getShardedPreferences(applicationContext)
        prefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun setup(context: Context) {
        super.setup(context)
        val filter = IntentFilter(GridQuickSettingsTile.ACTION_TOGGLE_STATE)
        filter.addAction(GridQuickSettingsTile.ACTION_UNPUBLISH)
        filter.addAction(ACTION_HIDE_OVERLAY)
        filter.addAction(ACTION_SHOW_OVERLAY)
        registerReceiver(receiver, filter)
    }

    override fun getPersistentNotification(context: Context): Notification {
        val pi = PendingIntent.getBroadcast(context, 0, Intent(if (isActive()) ACTION_HIDE_OVERLAY else ACTION_SHOW_OVERLAY), 0)
        val builder: Notification.Builder = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification.Builder(context, channel)
        } else {
            Notification.Builder(context)
        }
        val text = context.getString(if (isActive()) R.string.notif_content_hide_grid_overlay else R.string.notif_content_show_grid_overlay)
        builder.setSmallIcon(if (isActive()) R.drawable.ic_qs_grid_on else R.drawable.ic_qs_grid_off)
                .setContentTitle(context.getString(R.string.grid_qs_tile_label))
                .setContentText(text)
                .setStyle(Notification.BigTextStyle().bigText(text))
                .setContentIntent(pi)
        return builder.build()
    }

    private fun handleVisibility() {
        if (isActive()) show() else hide()
    }

    private fun isActive() : Boolean {
        val context = rootView?.context ?: return false
        return DesignerTools.gridOverlayOn(context)
    }

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
        handleVisibility()
    }

    private var receiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (GridQuickSettingsTile.ACTION_UNPUBLISH == action) {
                stopSelf()
            } else if (GridQuickSettingsTile.ACTION_TOGGLE_STATE == action) {
                val state = intent.getIntExtra(OnOffTileState.EXTRA_STATE, OnOffTileState.STATE_OFF)
                if (state == OnOffTileState.STATE_ON) {
                    stopSelf()
                }
            } else if (ACTION_HIDE_OVERLAY == action) {
                hide()
            } else if (ACTION_SHOW_OVERLAY == action) {
                show()
            }
        }
    }

    companion object {
        const val ACTION_HIDE_OVERLAY = "hide_grid_overlay"
        const val ACTION_SHOW_OVERLAY = "show_grid_overlay"
    }
}