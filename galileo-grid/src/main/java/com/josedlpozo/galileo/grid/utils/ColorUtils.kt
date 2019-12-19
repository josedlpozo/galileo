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
package com.josedlpozo.galileo.grid.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.josedlpozo.galileo.grid.R

internal object ColorUtils {
    fun getGridLineColor(context: Context): Int =
        PreferenceUtils.GridPreferences.getGridLineColor(
            context,
            ContextCompat.getColor(context, R.color.galileocolor_dualColorPickerDefaultPrimaryColor)
        )

    fun getKeylineColor(context: Context): Int =
        PreferenceUtils.GridPreferences.getKeylineColor(
            context,
            ContextCompat.getColor(
                context,
                R.color.galileocolor_dualColorPickerDefaultSecondaryColor
            )
        )
}
