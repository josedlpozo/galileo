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
