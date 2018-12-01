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
package com.josedlpozo.galileo.picker.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import com.josedlpozo.galileo.picker.overlays.ColorPickerOverlay
import com.josedlpozo.galileo.picker.overlays.GridOverlay
import com.josedlpozo.galileo.picker.qs.ColorPickerQuickSettingsTile
import com.josedlpozo.galileo.picker.qs.GridQuickSettingsTile
import com.josedlpozo.galileo.picker.ui.DesignerTools
import com.josedlpozo.galileo.picker.ui.ScreenRecordRequestActivity
import com.josedlpozo.galileo.picker.ui.StartOverlayActivity

object LaunchUtils {

    private fun publishGridOverlayTile(context: Context) {
        GridQuickSettingsTile.publishGridTile(context)
    }

    fun launchGridOverlay(context: Context) {
        startOverlayActivity(context, StartOverlayActivity.GRID_OVERLAY)
    }

    fun launchGridOverlayOrPublishTile(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            publishGridOverlayTile(context)
        } else {
            launchGridOverlay(context)
        }
    }

    private fun unPublishGridOverlayTile(context: Context) {
        GridQuickSettingsTile.unPublishGridTile(context)
    }

    fun cancelGridOverlay(context: Context) {
        val newIntent = Intent(context, GridOverlay::class.java)
        context.stopService(newIntent)
        PreferenceUtils.GridPreferences.setGridOverlayActive(context, false)
        PreferenceUtils.GridPreferences.setGridQsTileEnabled(context, false)
    }

    fun cancelGridOverlayOrUnpublishTile(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            unPublishGridOverlayTile(context)
            GridQuickSettingsTile.unPublishGridTile(context)
        } else {
            cancelGridOverlay(context)
        }
    }

    fun publishColorPickerTile(context: Context) {
        ColorPickerQuickSettingsTile.publishColorPickerTile(context)
    }

    fun launchColorPickerOverlay(context: Context) {
        startOverlayActivity(context, StartOverlayActivity.COLOR_PICKER_OVERLAY)
    }

    fun launchColorPickerOrPublishTile(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            publishColorPickerTile(context)
        } else {
            launchColorPickerOverlay(context)
        }
    }

    private fun unpublishColorPickerTile(context: Context) {
        ColorPickerQuickSettingsTile.unPublishColorPickerTile(context)
    }

    fun cancelColorPickerOverlay(context: Context) {
        val newIntent = Intent(context, ColorPickerOverlay::class.java)
        context.stopService(newIntent)
        PreferenceUtils.ColorPickerPreferences.setColorPickerActive(context, false)
        PreferenceUtils.ColorPickerPreferences.setColorPickerQsTileEnabled(context, false)
    }

    fun cancelColorPickerOrUnpublishTile(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            unpublishColorPickerTile(context)
        } else {
            cancelColorPickerOverlay(context)
        }
    }

    fun startColorPickerOrRequestPermission(context: Context) {
        if (DesignerTools.screenRecordResultCode == Activity.RESULT_OK && DesignerTools.screenRecordResultData != null) {
            val newIntent = Intent(context, ColorPickerOverlay::class.java)
            context.startService(newIntent)
            PreferenceUtils.ColorPickerPreferences.setColorPickerActive(context, true)
            PreferenceUtils.ColorPickerPreferences.setColorPickerQsTileEnabled(context, true)
        } else {
            val intent = Intent(context, ScreenRecordRequestActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    private fun startOverlayActivity(context: Context, overlayType: Int) {
        val intent = Intent(context, StartOverlayActivity::class.java)
        intent.putExtra(StartOverlayActivity.EXTRA_OVERLAY_TYPE, overlayType)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
