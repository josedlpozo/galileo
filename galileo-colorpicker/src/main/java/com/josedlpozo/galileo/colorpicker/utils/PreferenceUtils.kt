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
package com.josedlpozo.galileo.colorpicker.utils

import android.content.Context
import android.content.SharedPreferences

internal object PreferenceUtils {
    private const val PREFERENCES_FILE = "com.josedlpozo.galileo.colorpicker.design_preferences"

    fun getShardedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_FILE, 0)
    }

    private fun putBoolean(context: Context, key: String, value: Boolean) {
        getShardedPreferences(context).edit().putBoolean(key, value).apply()
    }

    private fun getBoolean(context: Context, key: String, defValue: Boolean): Boolean {
        return getShardedPreferences(context).getBoolean(key, defValue)
    }

    object ColorPickerPreferences {
        const val KEY_COLOR_PICKER_ENABLED = "color_picker_enabled"

        fun setColorPickerEnabled(context: Context, enabled: Boolean) {
            putBoolean(context, KEY_COLOR_PICKER_ENABLED, enabled)
        }

        fun getColorPickerEnabled(context: Context, defValue: Boolean): Boolean {
            return getBoolean(context, KEY_COLOR_PICKER_ENABLED, defValue)
        }
    }

}