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
package com.josedlpozo.galileo.picker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.josedlpozo.galileo.picker.overlays.ColorPickerOverlay;
import com.josedlpozo.galileo.picker.overlays.GridOverlay;
import com.josedlpozo.galileo.picker.qs.ColorPickerQuickSettingsTile;
import com.josedlpozo.galileo.picker.qs.GridQuickSettingsTile;
import com.josedlpozo.galileo.picker.ui.DesignerTools;
import com.josedlpozo.galileo.picker.ui.ScreenRecordRequestActivity;
import com.josedlpozo.galileo.picker.ui.StartOverlayActivity;

public class LaunchUtils {
    public static boolean isCyanogenMod(Context context) {
        return context.getPackageManager().hasSystemFeature("org.cyanogenmod.theme");
    }

    public static void publishGridOverlayTile(Context context, int state) {
        GridQuickSettingsTile.publishGridTile(context, state);
    }

    public static void launchGridOverlay(Context context) {
        startOverlayActivity(context, StartOverlayActivity.GRID_OVERLAY);
    }

    public static void lauchGridOverlayOrPublishTile(Context context, int state) {
        if (isCyanogenMod(context) && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            publishGridOverlayTile(context, state);
        } else {
            launchGridOverlay(context);
        }
    }

    private static void unpublishGridOverlayTile(Context context) {
        GridQuickSettingsTile.unpublishGridTile(context);
    }

    public static void cancelGridOverlay(Context context) {
        Intent newIntent = new Intent(context, GridOverlay.class);
        context.stopService(newIntent);
        PreferenceUtils.GridPreferences.setGridOverlayActive(context, false);
        PreferenceUtils.GridPreferences.setGridQsTileEnabled(context, false);
    }

    public static void cancelGridOverlayOrUnpublishTile(Context context) {
        if (isCyanogenMod(context) && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            unpublishGridOverlayTile(context);
            GridQuickSettingsTile.unpublishGridTile(context);
        } else {
            cancelGridOverlay(context);
        }
    }

    public static void publishColorPickerTile(Context context, int state) {
        ColorPickerQuickSettingsTile.publishColorPickerTile(context, state);
    }

    public static void launchColorPickerOverlay(Context context) {
        startOverlayActivity(context, StartOverlayActivity.COLOR_PICKER_OVERLAY);
    }

    public static void lauchColorPickerOrPublishTile(Context context, int state) {
        if (isCyanogenMod(context) && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            publishColorPickerTile(context, state);
        } else {
            launchColorPickerOverlay(context);
        }
    }

    private static void unpublishColorPickerTile(Context context) {
        ColorPickerQuickSettingsTile.unpublishColorPickerTile(context);
    }

    public static void cancelColorPickerOverlay(Context context) {
        Intent newIntent = new Intent(context, ColorPickerOverlay.class);
        context.stopService(newIntent);
        PreferenceUtils.ColorPickerPreferences.setColorPickerActive(context, false);
        PreferenceUtils.ColorPickerPreferences.setColorPickerQsTileEnabled(context, false);
    }

    public static void cancelColorPickerOrUnpublishTile(Context context) {
        if (isCyanogenMod(context) && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            unpublishColorPickerTile(context);
        } else {
            cancelColorPickerOverlay(context);
        }
    }

    public static void startColorPickerOrRequestPermission(Context context) {
        if (DesignerTools.INSTANCE.getScreenRecordResultCode() == Activity.RESULT_OK && DesignerTools.INSTANCE.getScreenRecordResultData() != null) {
            Intent newIntent = new Intent(context, ColorPickerOverlay.class);
            context.startService(newIntent);
            PreferenceUtils.ColorPickerPreferences.setColorPickerActive(context, true);
            PreferenceUtils.ColorPickerPreferences.setColorPickerQsTileEnabled(context, true);
        } else {
            Intent intent = new Intent(context, ScreenRecordRequestActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    private static void startOverlayActivity(Context context, int overlayType) {
        Intent intent = new Intent(context, StartOverlayActivity.class);
        intent.putExtra(StartOverlayActivity.EXTRA_OVERLAY_TYPE, overlayType);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
