package com.josedlpozo.galileo.picker.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.josedlpozo.galileo.picker.utils.PreferenceUtils

object DesignerTools {

    var screenRecordResultCode = Activity.RESULT_CANCELED
        private set
    var screenRecordResultData: Intent? = null
        private set

    fun gridOverlayOn(context: Context) : Boolean = PreferenceUtils.GridPreferences.getGridQsTileEnabled(context, false)

    fun colorPickerOn(context: Context) : Boolean = PreferenceUtils.ColorPickerPreferences.getColorPickerQsTileEnabled(context, false)

    fun setScreenRecordPermissionData(resultCode: Int, resultData: Intent) {
        screenRecordResultCode = resultCode
        screenRecordResultData = resultData
    }

    fun setGridOverlayOn(context: Context, value: Boolean) {
        PreferenceUtils.GridPreferences.setGridQsTileEnabled(context, value)
    }

    fun setColorPickerOn(context: Context, value: Boolean) {
        PreferenceUtils.ColorPickerPreferences.setColorPickerQsTileEnabled(context, value)
    }
}
