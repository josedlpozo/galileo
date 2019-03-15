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
package com.josedlpozo.galileo.floaticon

import android.content.Context
import android.content.SharedPreferences

object FloatIconPreferenceUtils {
    private const val PREFERENCES_FILE = "com.josedlpozo.galileo.float_icon"
    private const val KEY_X_POSITION = "x_position"
    private const val KEY_Y_POSITION = "y_position"

    fun setX(context: Context, x: Int) {
        putInt(context, KEY_X_POSITION, x)
    }

    fun setY(context: Context, y: Int) {
        putInt(context, KEY_Y_POSITION, y)
    }

    fun getX(context: Context, defValue: Int = 0): Int {
        return getInt(context, KEY_X_POSITION, defValue)
    }

    fun getY(context: Context, defValue: Int = 0): Int {
        return getInt(context, KEY_Y_POSITION, defValue)
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_FILE, 0)
    }

    private fun putBoolean(context: Context, key: String, value: Boolean) {
        getSharedPreferences(context).edit().putBoolean(key, value).apply()
    }

    private fun getBoolean(context: Context, key: String, defValue: Boolean): Boolean {
        return getSharedPreferences(context).getBoolean(key, defValue)
    }

    private fun putInt(context: Context, key: String, value: Int) {
        getSharedPreferences(context).edit().putInt(key, value).apply()
    }

    private fun getInt(context: Context, key: String, defValue: Int): Int {
        return getSharedPreferences(context).getInt(key, defValue)
    }

}