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
package com.josedlpozo.galileo.picker.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import com.josedlpozo.galileo.picker.overlays.GridOverlay
import com.josedlpozo.galileo.picker.utils.LaunchUtils
import com.josedlpozo.galileo.picker.utils.PreferenceUtils

class StartOverlayActivity : Activity() {

    private var overlayType = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        if (intent.hasExtra(EXTRA_OVERLAY_TYPE)) {
            overlayType = intent.getIntExtra(EXTRA_OVERLAY_TYPE, -1)
            if (Settings.canDrawOverlays(this)) {
                startOverlayService(overlayType)
                finish()
            } else {
                val closeDialogsIntent = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
                sendBroadcast(closeDialogsIntent)
                val newIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName"))
                startActivityForResult(newIntent, REQUEST_OVERLAY_PERMSSISION)
            }
        } else {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_OVERLAY_PERMSSISION) {
            if (Settings.canDrawOverlays(this)) {
                startOverlayService(overlayType)
            }
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun startOverlayService(overlayType: Int) {
        when (overlayType) {
            GRID_OVERLAY -> {
                val newIntent = Intent(this, GridOverlay::class.java)
                this.startService(newIntent)
                PreferenceUtils.GridPreferences.setGridOverlayActive(this, true)
                PreferenceUtils.GridPreferences.setGridQsTileEnabled(this, true)
            }
            COLOR_PICKER_OVERLAY -> LaunchUtils.startColorPickerOrRequestPermission(this)
        }
    }

    companion object {
        private const val REQUEST_OVERLAY_PERMSSISION = 42

        const val EXTRA_OVERLAY_TYPE = "overlayType"
        const val GRID_OVERLAY = 0
        const val COLOR_PICKER_OVERLAY = 2
    }
}
