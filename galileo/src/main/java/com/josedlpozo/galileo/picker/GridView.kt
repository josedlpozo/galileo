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

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.items.GalileoItem
import com.josedlpozo.galileo.picker.ui.DesignerTools
import com.josedlpozo.galileo.picker.ui.DualColorPickerDialog
import com.josedlpozo.galileo.picker.utils.ColorUtils
import com.josedlpozo.galileo.picker.utils.LaunchUtils
import com.josedlpozo.galileo.picker.utils.PreferenceUtils
import com.josedlpozo.galileo.picker.widget.DualColorPicker
import com.josedlpozo.galileo.picker.widget.GridPreview
import com.josedlpozo.galileo.picker.widget.VerticalSeekBar

internal class GridView internal constructor(context: Context) : LinearLayout(context), GalileoItem,
        SharedPreferences.OnSharedPreferenceChangeListener,
        CompoundButton.OnCheckedChangeListener {

    override val name: String = "Grid"
    override val view: View = this
    override val icon: Int = R.drawable.ic_qs_grid_on
    override fun snapshot(): String = ""

    private val dualColorPicker: DualColorPicker
    private val cbIncludeKeylines: CheckBox
    private val cbIncludeCustomGrid: CheckBox
    private val sbColumnSizer: SeekBar
    private val sbRowSizer: VerticalSeekBar
    private val gridPreview: GridPreview
    private val swGrid: Switch

    init {
        LayoutInflater.from(context).inflate(R.layout.card_layout, this, true)
        PreferenceUtils.getShardedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this)

        val mHeaderTitle = findViewById<TextView>(R.id.header_title)
        val mCardContent = findViewById<FrameLayout>(R.id.card_content)
        swGrid = findViewById(R.id.enable_switch)
        swGrid.setOnCheckedChangeListener(this)
        swGrid.isChecked = DesignerTools.gridOverlayOn(getContext())

        mHeaderTitle?.setText(R.string.header_title_grid_overlay)

        backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.galileo_colorGridOverlayCardTint))


        val view = LayoutInflater.from(context).inflate(R.layout.grid_overlay_content, mCardContent, true)

        cbIncludeKeylines = view.findViewById(R.id.include_keylines)
        cbIncludeCustomGrid = view.findViewById(R.id.include_custom_grid_size)
        sbColumnSizer = view.findViewById(R.id.column_sizer)
        sbColumnSizer.progress = (PreferenceUtils.GridPreferences.getGridColumnSize(getContext(), 8) - 4) / 2
        sbRowSizer = view.findViewById(R.id.row_sizer)
        sbRowSizer.progress = (PreferenceUtils.GridPreferences.getGridRowSize(getContext(), 8) - 4) / 2
        gridPreview = view.findViewById(R.id.grid_preview)
        gridPreview.setColumnSize(PreferenceUtils.GridPreferences.getGridColumnSize(getContext(), 8))
        gridPreview.setRowSize(PreferenceUtils.GridPreferences.getGridRowSize(getContext(), 8))

        val mSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val size = 4 + progress * 2
                if (seekBar === sbColumnSizer) {
                    gridPreview.setColumnSize(size)
                    PreferenceUtils.GridPreferences.setGridColumnSize(getContext(), size)
                } else if (seekBar === sbRowSizer) {
                    gridPreview.setRowSize(size)
                    PreferenceUtils.GridPreferences.setGridRowSize(getContext(), size)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }

        sbColumnSizer.setOnSeekBarChangeListener(mSeekBarChangeListener)
        sbRowSizer.setOnSeekBarChangeListener(mSeekBarChangeListener)

        val mCheckChangedListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView === cbIncludeKeylines) {
                PreferenceUtils.GridPreferences.setShowKeylines(getContext(), isChecked)
            } else if (buttonView === cbIncludeCustomGrid) {
                PreferenceUtils.GridPreferences.setUseCustomGridSize(getContext(), isChecked)
                if (isChecked) {
                    PreferenceUtils.GridPreferences.setGridColumnSize(getContext(), gridPreview.getColumnSize())
                    PreferenceUtils.GridPreferences.setGridRowSize(getContext(), gridPreview.getRowSize())
                }
                sbColumnSizer.isEnabled = isChecked
                sbRowSizer.isEnabled = isChecked
            }
        }

        cbIncludeKeylines.setChecked(PreferenceUtils.GridPreferences.getShowKeylines(context, false))
        cbIncludeKeylines.setOnCheckedChangeListener(mCheckChangedListener)

        setIncludeCustomGridLines(PreferenceUtils.GridPreferences.getUseCustomGridSize(context, false))
        cbIncludeCustomGrid.setOnCheckedChangeListener(mCheckChangedListener)

        sbRowSizer.setOnTouchListener { v1, event ->
            when (event.getAction()) {
                MotionEvent.ACTION_DOWN -> v1.getParent().requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP -> v1.getParent().requestDisallowInterceptTouchEvent(false)
            }
            v1.onTouchEvent(event)
            true
        }

        dualColorPicker = view.findViewById(R.id.color_picker)

        dualColorPicker.setOnClickListener { v12 ->
            val fm = (context as Activity).fragmentManager
            val dualColorPickerDialog = DualColorPickerDialog()
            dualColorPickerDialog.show(fm, "color_picker_dialog")
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (PreferenceUtils.GridPreferences.KEY_GRID_LINE_COLOR == key) {
            dualColorPicker.primaryColor = ColorUtils.getGridLineColor(context)
        } else if (PreferenceUtils.GridPreferences.KEY_KEYLINE_COLOR == key) {
            dualColorPicker.secondaryColor = ColorUtils.getKeylineColor(context)
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (isChecked == DesignerTools.gridOverlayOn(context)) {
            return
        }
        if (isChecked) {
            LaunchUtils.launchGridOverlayOrPublishTile(context)
        } else {
            LaunchUtils.cancelGridOverlayOrUnpublishTile(context)
        }
    }

    private fun setIncludeCustomGridLines(include: Boolean) {
        cbIncludeCustomGrid.isChecked = include
        sbColumnSizer.isEnabled = include
        sbRowSizer.isEnabled = include
    }

}