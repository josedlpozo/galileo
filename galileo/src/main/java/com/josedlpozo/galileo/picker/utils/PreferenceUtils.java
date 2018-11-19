package com.josedlpozo.galileo.picker.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {
    private static final String PREFERENCES_FILE = "com.josedlpozo.galileo.design_preferences";

    public static SharedPreferences getShardedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_FILE, 0);
    }

    private static void putBoolean(Context context, String key, boolean value) {
        getShardedPreferences(context).edit().putBoolean(key, value).apply();
    }

    private static boolean getBoolean(Context context, String key, boolean defValue) {
        return getShardedPreferences(context).getBoolean(key, defValue);
    }

    private static void putInt(Context context, String key, int value) {
        getShardedPreferences(context).edit().putInt(key, value).apply();
    }

    private static int getInt(Context context, String key, int defValue) {
        return getShardedPreferences(context).getInt(key, defValue);
    }

    private static void putString(Context context, String key, String value) {
        getShardedPreferences(context).edit().putString(key, value).apply();
    }

    public static class GridPreferences {
        public static final String KEY_GRID_QS_TILE = "grid_qs_tile";
        public static final String KEY_SHOW_GRID = "grid_increments";
        public static final String KEY_SHOW_KEYLINES = "keylines";
        public static final String KEY_USE_CUSTOM_GRID_SIZE = "use_custom_grid_size";
        public static final String KEY_GRID_COLUMN_SIZE = "grid_column_size";
        public static final String KEY_GRID_ROW_SIZE = "grid_row_size";
        public static final String KEY_GRID_LINE_COLOR = "grid_line_color";
        public static final String KEY_KEYLINE_COLOR = "keyline_color";
        public static final String KEY_GRID_OVERLAY_ACTIVE = "grid_overlay_active";

        public static void setGridQsTileEnabled(Context context, boolean enabled) {
            putBoolean(context, KEY_GRID_QS_TILE, enabled);
        }

        public static boolean getGridQsTileEnabled(Context context, boolean defValue) {
            return getBoolean(context, KEY_GRID_QS_TILE, defValue);
        }

        public static boolean getShowGrid(Context context, boolean defValue) {
            return getBoolean(context, KEY_SHOW_GRID, defValue);
        }

        public static void setShowKeylines(Context context, boolean show) {
            putBoolean(context, KEY_SHOW_KEYLINES, show);
        }

        public static boolean getShowKeylines(Context context, boolean defValue) {
            return getBoolean(context, KEY_SHOW_KEYLINES, defValue);
        }

        public static void setUseCustomGridSize(Context context, boolean use) {
            putBoolean(context, KEY_USE_CUSTOM_GRID_SIZE, use);
        }

        public static boolean getUseCustomGridSize(Context context, boolean defValue) {
            return getBoolean(context, KEY_USE_CUSTOM_GRID_SIZE, defValue);
        }

        public static void setGridColumnSize(Context context, int stepSize) {
            putInt(context, KEY_GRID_COLUMN_SIZE, stepSize);
        }

        public static int getGridColumnSize(Context context, int defValue) {
            return getInt(context, KEY_GRID_COLUMN_SIZE, defValue);
        }

        public static void setGridRowSize(Context context, int stepSize) {
            putInt(context, KEY_GRID_ROW_SIZE, stepSize);
        }

        public static int getGridRowSize(Context context, int defValue) {
            return getInt(context, KEY_GRID_ROW_SIZE, defValue);
        }

        public static void setGridLineColor(Context context, int color) {
            putInt(context, KEY_GRID_LINE_COLOR, color);
        }

        public static int getGridLineColor(Context context, int defValue) {
            return getInt(context, KEY_GRID_LINE_COLOR, defValue);
        }

        public static void setKeylineColor(Context context, int color) {
            putInt(context, KEY_KEYLINE_COLOR, color);
        }

        public static int getKeylineColor(Context context, int defValue) {
            return getInt(context, KEY_KEYLINE_COLOR, defValue);
        }

        public static void setGridOverlayActive(Context context, boolean active) {
            putBoolean(context, KEY_GRID_OVERLAY_ACTIVE, active);
        }

        public static boolean getGridOverlayActive(Context context, boolean defValue) {
            return getBoolean(context, KEY_GRID_OVERLAY_ACTIVE, defValue);
        }
    }

    public static class MockPreferences {
        public static final String KEY_MOCKUP_OVERLAY_PORTRAIT = "mockup_overlay_portrait";
        public static final String KEY_MOCKUP_OVERLAY_LANDSCAPE = "mock_overlay_landscape";

        public static void setPortraitMocupkOverlay(Context context, String pathOrUri) {
            putString(context, KEY_MOCKUP_OVERLAY_PORTRAIT, pathOrUri);
        }

        public static void setLandscapeMocupkOverlay(Context context, String pathOrUri) {
            putString(context, KEY_MOCKUP_OVERLAY_LANDSCAPE, pathOrUri);
        }
    }

    public static class ColorPickerPreferences {
        public static final String KEY_COLOR_PICKER_QS_TILE = "color_picker_qs_tile";
        public static final String KEY_COLOR_PICKER_ACTIVE = "color_picker_active";

        public static void setColorPickerQsTileEnabled(Context context, boolean enabled) {
            putBoolean(context, KEY_COLOR_PICKER_QS_TILE, enabled);
        }

        public static boolean getColorPickerQsTileEnabled(Context context, boolean defValue) {
            return getBoolean(context, KEY_COLOR_PICKER_QS_TILE, defValue);
        }

        public static void setColorPickerActive(Context context, boolean active) {
            putBoolean(context, KEY_COLOR_PICKER_ACTIVE, active);
        }

        public static boolean getColorPickerActive(Context context, boolean defValue) {
            return getBoolean(context, KEY_COLOR_PICKER_ACTIVE, defValue);
        }
    }

}
