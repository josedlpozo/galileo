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
package com.josedlpozo.galileo.picker

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.items.GalileoItem
import com.josedlpozo.galileo.picker.qs.OnOffTileState
import com.josedlpozo.galileo.picker.ui.DesignerTools
import com.josedlpozo.galileo.picker.utils.LaunchUtils
import com.josedlpozo.galileo.picker.utils.PreferenceUtils

internal class PickerView internal constructor(context: Context) : LinearLayout(context), GalileoItem, CompoundButton.OnCheckedChangeListener {

    override val name: String = "Picker"
    override val view: View = this
    override val icon: Int = R.drawable.ic_qs_colorpicker_on
    override fun snapshot(): String = ""

    private val swColorPicker: Switch

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked == DesignerTools.colorPickerOn(context)) return

        if (isChecked) {
            enableFeature(true)
        } else {
            enableFeature(false)
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.card_layout, this)
        backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.galileo_colorColorPickerCardTint))

        val mHeaderTitle = findViewById<TextView>(R.id.header_title)
        swColorPicker = findViewById(R.id.enable_switch)
        swColorPicker.setOnCheckedChangeListener(this)
        swColorPicker.isChecked = DesignerTools.colorPickerOn(context)

        mHeaderTitle?.setText(R.string.header_title_color_picker)
    }

    private fun enableFeature(enable: Boolean) {
        if (enable) {
            LaunchUtils.lauchColorPickerOrPublishTile(context, if (PreferenceUtils.ColorPickerPreferences.getColorPickerActive(context, false))
                OnOffTileState.STATE_ON
            else
                OnOffTileState.STATE_OFF)
        } else {
            LaunchUtils.cancelColorPickerOrUnpublishTile(context)
        }
    }

}