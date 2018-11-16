package com.josedlpozo.galileo.picker.qs;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.picker.utils.LaunchUtils;
import com.josedlpozo.galileo.picker.utils.PreferenceUtils;
import cyanogenmod.app.CMStatusBarManager;
import cyanogenmod.app.CustomTile;

public class ColorPickerQuickSettingsTile {

    private static final String TAG = ColorPickerQuickSettingsTile.class.getSimpleName();
    public static final String ACTION_TOGGLE_STATE = "org.cyanogenmod.designertools.action.TOGGLE_COLOR_PICKER_STATE";
    public static final String ACTION_UNPUBLISH = "org.cyanogenmod.designertools.action.UNPUBLISH_COLOR_PICKER_TILE";
    public static final int TILE_ID = 5000;

    public static void publishColorPickerTile(Context context) {
        publishColorPickerTile(context, OnOffTileState.STATE_OFF);
    }

    public static void publishColorPickerTile(Context context, int state) {
        Intent intent = new Intent(ACTION_TOGGLE_STATE);
        intent.putExtra(OnOffTileState.EXTRA_STATE, state);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int iconResId = state == OnOffTileState.STATE_OFF ? R.drawable.ic_qs_colorpicker_off : R.drawable.ic_qs_colorpicker_on;
        CustomTile tile = new CustomTile.Builder(context).setOnClickIntent(pi)
                                                         .setLabel(context.getString(R.string.color_picker_qs_tile_label))
                                                         .setIcon(iconResId)
                                                         .build();
        CMStatusBarManager.getInstance(context).publishTile(TAG, TILE_ID, tile);
        PreferenceUtils.ColorPickerPreferences.setColorPickerQsTileEnabled(context, true);
    }

    public static void unpublishColorPickerTile(Context context) {
        CMStatusBarManager.getInstance(context).removeTile(TAG, TILE_ID);
        PreferenceUtils.ColorPickerPreferences.setColorPickerQsTileEnabled(context, false);
        Intent intent = new Intent(ColorPickerQuickSettingsTile.ACTION_UNPUBLISH);
        context.sendBroadcast(intent);
    }

    public static class ClickBroadcastReceiver extends BroadcastReceiver {

        @Override public void onReceive(Context context, Intent intent) {
            if (PreferenceUtils.ColorPickerPreferences.getColorPickerQsTileEnabled(context, false)) {
                int state = intent.getIntExtra(OnOffTileState.EXTRA_STATE, OnOffTileState.STATE_OFF);
                if (state == OnOffTileState.STATE_OFF) {
                    publishColorPickerTile(context, OnOffTileState.STATE_ON);
                    LaunchUtils.startColorPickerOrRequestPermission(context);
                } else {
                    publishColorPickerTile(context, OnOffTileState.STATE_OFF);
                    PreferenceUtils.ColorPickerPreferences.setColorPickerActive(context, false);
                }
            }
        }
    }
}
