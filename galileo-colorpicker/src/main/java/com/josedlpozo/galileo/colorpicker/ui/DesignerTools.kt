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

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.josedlpozo.galileo.colorpicker.utils.PreferenceUtils

internal object DesignerTools {

    var screenRecordResultCode = Activity.RESULT_CANCELED
        private set
    var screenRecordResultData: Intent? = null
        private set

    fun colorPickerOn(context: Context) : Boolean = PreferenceUtils.ColorPickerPreferences.getColorPickerEnabled(context, false)

    fun setScreenRecordPermissionData(resultCode: Int, resultData: Intent) {
        screenRecordResultCode = resultCode
        screenRecordResultData = resultData
    }

    fun setColorPickerOn(context: Context, value: Boolean) {
        PreferenceUtils.ColorPickerPreferences.setColorPickerEnabled(context, value)
    }
}
