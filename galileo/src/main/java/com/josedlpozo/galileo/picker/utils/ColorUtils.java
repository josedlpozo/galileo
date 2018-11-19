package com.josedlpozo.galileo.picker.utils;

import android.content.Context;
import com.josedlpozo.galileo.R;

public class ColorUtils {
    public static int getGridLineColor(Context context) {
        return PreferenceUtils.GridPreferences.getGridLineColor(context,
                context.getColor(R.color.dualColorPickerDefaultPrimaryColor));
    }

    public static int getKeylineColor(Context context) {
        return PreferenceUtils.GridPreferences.getKeylineColor(context,
                context.getColor(R.color.dualColorPickerDefaultSecondaryColor));
    }
}
