/*
 * Copyright (C) 2016 The CyanogenMod Project
 *
 * Modified Work: Copyright (c) 2018 fr4nk1
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
package com.josedlpozo.galileo.picker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.josedlpozo.galileo.picker.qs.ColorPickerQuickSettingsTile;
import com.josedlpozo.galileo.picker.qs.GridQuickSettingsTile;
import com.josedlpozo.galileo.picker.utils.LaunchUtils;
import com.josedlpozo.galileo.picker.utils.PreferenceUtils;

public class BootReceiver extends BroadcastReceiver {

    public BootReceiver() { }

    @Override public void onReceive(Context context, Intent intent) {
        final boolean isCm = LaunchUtils.isCyanogenMod(context);
        if (PreferenceUtils.GridPreferences.getGridQsTileEnabled(context, false)) {
            PreferenceUtils.GridPreferences.setGridOverlayActive(context, false);
            if (isCm) { GridQuickSettingsTile.publishGridTile(context); }
        }
        if (PreferenceUtils.ColorPickerPreferences.getColorPickerQsTileEnabled(context, false)) {
            PreferenceUtils.ColorPickerPreferences.setColorPickerActive(context, false);
            if (isCm) { ColorPickerQuickSettingsTile.publishColorPickerTile(context); }
        }
    }
}
