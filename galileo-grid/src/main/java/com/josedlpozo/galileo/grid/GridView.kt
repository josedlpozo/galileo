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
package com.josedlpozo.galileo.grid

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.josedlpozo.galileo.grid.ui.DesignerTools
import com.josedlpozo.galileo.grid.ui.DualColorPickerDialog
import com.josedlpozo.galileo.grid.utils.ColorUtils
import com.josedlpozo.galileo.grid.utils.PreferenceUtils
import com.josedlpozo.galileo.grid.widget.DualColorPicker
import com.josedlpozo.galileo.grid.widget.GridPreview
import com.josedlpozo.galileo.grid.widget.VerticalSeekBar

internal class GridView internal constructor(context: Context) : LinearLayout(context),
    SharedPreferences.OnSharedPreferenceChangeListener,
    CompoundButton.OnCheckedChangeListener {

    private val dualColorPicker: DualColorPicker
    private val cbIncludeKeylines: CheckBox
    private val cbIncludeCustomGrid: CheckBox
    private val sbColumnSizer: SeekBar
    private val sbRowSizer: VerticalSeekBar
    private val gridPreview: GridPreview
    private val swGrid: Switch

    init {
        LayoutInflater.from(context).inflate(R.layout.card_layout, this, true)
        PreferenceUtils.getShardedPreferences(getContext())
            .registerOnSharedPreferenceChangeListener(this)

        val mHeaderTitle = findViewById<TextView>(R.id.header_title)
        val mCardContent = findViewById<FrameLayout>(R.id.card_content)
        swGrid = findViewById(R.id.enable_switch)
        swGrid.setOnCheckedChangeListener(this)
        swGrid.isChecked = DesignerTools.gridOverlayOn(getContext())

        mHeaderTitle?.setText(R.string.header_title_grid_overlay)


        val view =
            LayoutInflater.from(context).inflate(R.layout.grid_overlay_content, mCardContent, true)

        cbIncludeKeylines = view.findViewById(R.id.include_keylines)
        cbIncludeCustomGrid = view.findViewById(R.id.include_custom_grid_size)
        sbColumnSizer = view.findViewById(R.id.column_sizer)
        sbColumnSizer.progress =
            (PreferenceUtils.GridPreferences.getGridColumnSize(getContext(), 8) - 4) / 2
        sbRowSizer = view.findViewById(R.id.row_sizer)
        sbRowSizer.progress =
            (PreferenceUtils.GridPreferences.getGridRowSize(getContext(), 8) - 4) / 2
        gridPreview = view.findViewById(R.id.grid_preview)
        gridPreview.setColumnSize(
            PreferenceUtils.GridPreferences.getGridColumnSize(
                getContext(),
                8
            )
        )
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

        val mCheckChangedListener =
            CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView === cbIncludeKeylines) {
                    PreferenceUtils.GridPreferences.setShowKeylines(getContext(), isChecked)
                } else if (buttonView === cbIncludeCustomGrid) {
                    PreferenceUtils.GridPreferences.setUseCustomGridSize(getContext(), isChecked)
                    if (isChecked) {
                        PreferenceUtils.GridPreferences.setGridColumnSize(
                            getContext(),
                            gridPreview.getColumnSize()
                        )
                        PreferenceUtils.GridPreferences.setGridRowSize(
                            getContext(),
                            gridPreview.getRowSize()
                        )
                    }
                    sbColumnSizer.isEnabled = isChecked
                    sbRowSizer.isEnabled = isChecked
                }
            }

        cbIncludeKeylines.isChecked =
            PreferenceUtils.GridPreferences.getShowKeylines(context, false)
        cbIncludeKeylines.setOnCheckedChangeListener(mCheckChangedListener)

        setIncludeCustomGridLines(
            PreferenceUtils.GridPreferences.getUseCustomGridSize(
                context,
                false
            )
        )
        cbIncludeCustomGrid.setOnCheckedChangeListener(mCheckChangedListener)

        sbRowSizer.setOnTouchListener { v1, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> v1.parent.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP -> v1.parent.requestDisallowInterceptTouchEvent(false)
            }
            v1.onTouchEvent(event)
            true
        }

        dualColorPicker = view.findViewById(R.id.color_picker)

        dualColorPicker.setOnClickListener {
            val fm = (context as AppCompatActivity).supportFragmentManager
            val dualColorPickerDialog = DualColorPickerDialog()
            dualColorPickerDialog.show(fm, "color_picker_dialog")
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (PreferenceUtils.GridPreferences.KEY_GRID_LINE_COLOR == key) {
            dualColorPicker.primaryColor = ColorUtils.getGridLineColor(context)
        } else if (PreferenceUtils.GridPreferences.KEY_KEYLINE_COLOR == key) {
            dualColorPicker.secondaryColor = ColorUtils.getKeylineColor(context)
        } else if (PreferenceUtils.GridPreferences.KEY_GRID_TILE == key) {
            swGrid.isChecked = DesignerTools.gridOverlayOn(context)
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (isChecked == DesignerTools.gridOverlayOn(context)) {
            return
        }
        DesignerTools.setGridOverlayOn(context, isChecked)
    }

    private fun setIncludeCustomGridLines(include: Boolean) {
        cbIncludeCustomGrid.isChecked = include
        sbColumnSizer.isEnabled = include
        sbRowSizer.isEnabled = include
    }

}