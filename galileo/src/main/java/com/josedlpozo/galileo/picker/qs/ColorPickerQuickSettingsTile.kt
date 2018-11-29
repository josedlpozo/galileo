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
package com.josedlpozo.galileo.picker.qs

import android.content.Context
import android.content.Intent

import com.josedlpozo.galileo.picker.utils.PreferenceUtils

object ColorPickerQuickSettingsTile {

    const val ACTION_TOGGLE_STATE = "org.cyanogenmod.designertools.action.TOGGLE_COLOR_PICKER_STATE"
    const val ACTION_UNPUBLISH = "org.cyanogenmod.designertools.action.UNPUBLISH_COLOR_PICKER_TILE"

    fun publishColorPickerTile(context: Context) {
        PreferenceUtils.ColorPickerPreferences.setColorPickerQsTileEnabled(context, true)
    }

    fun unPublishColorPickerTile(context: Context) {
        PreferenceUtils.ColorPickerPreferences.setColorPickerQsTileEnabled(context, false)
        val intent = Intent(ColorPickerQuickSettingsTile.ACTION_UNPUBLISH)
        context.sendBroadcast(intent)
    }
}
