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
package com.josedlpozo.galileo.picker.service.qs

import android.annotation.TargetApi
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Build.VERSION_CODES
import android.service.quicksettings.TileService
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.picker.ui.DesignerTools
import com.josedlpozo.galileo.picker.utils.LaunchUtils

@TargetApi(VERSION_CODES.N)
class ColorPickerTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        updateTile(DesignerTools.colorPickerOn(this))
    }

    override fun onClick() {
        super.onClick()
        val isOn = DesignerTools.colorPickerOn(this)
        LaunchUtils.publishColorPickerTile(this)
        if (isOn) {
            LaunchUtils.cancelColorPickerOverlay(this)
        } else {
            LaunchUtils.launchColorPickerOverlay(this)
        }
        updateTile(!isOn)
    }

    private fun updateTile(isOn: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return
        val tile = qsTile
        tile.icon = Icon.createWithResource(this, if (isOn) R.drawable.ic_qs_colorpicker_on else R.drawable.ic_qs_colorpicker_off)
        tile.updateTile()
    }
}
