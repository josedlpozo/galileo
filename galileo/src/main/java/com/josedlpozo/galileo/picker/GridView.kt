package com.josedlpozo.galileo.picker

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import com.josedlpozo.galileo.items.GalileoItem
import com.josedlpozo.galileo.picker.qs.OnOffTileState
import com.josedlpozo.galileo.picker.ui.DesignerTools
import com.josedlpozo.galileo.picker.utils.ColorUtils
import com.josedlpozo.galileo.picker.utils.LaunchUtils
import com.josedlpozo.galileo.picker.utils.PreferenceUtils
import com.josedlpozo.galileo.picker.widget.DualColorPicker
import com.josedlpozo.galileo.picker.widget.GridPreview
import com.josedlpozo.galileo.picker.widget.VerticalSeekBar
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.picker.ui.DualColorPickerDialog

internal class GridView @JvmOverloads internal constructor(context: Context) : LinearLayout(context), GalileoItem,
                                                                               SharedPreferences.OnSharedPreferenceChangeListener,
                                                                               CompoundButton.OnCheckedChangeListener {

    override val name: String = "Grid"
    override val galileoView: View = this
    override val icon: Int = R.drawable.ic_qs_grid_on
    override fun snapshot(): String = ""

    private val mDualColorPicker: DualColorPicker
    private val mIncludeKeylines: CheckBox
    private val mIncudeCustomGrid: CheckBox
    private val mColumnSizer: SeekBar
    private val mRowSizer: VerticalSeekBar
    private val mGridPreview: GridPreview

    init {
        LayoutInflater.from(context).inflate(R.layout.card_layout, this, true)
        PreferenceUtils.getShardedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this)

        val mHeaderTitle = findViewById<TextView>(R.id.header_title)
        val mHeaderSummary = findViewById<TextView>(R.id.header_summary)
        val mEnabledSwitch = findViewById<Switch>(R.id.enable_switch)
        val mCardContent = findViewById<FrameLayout>(R.id.card_content)
        mEnabledSwitch.setOnCheckedChangeListener(this)
        mEnabledSwitch.isChecked = DesignerTools.gridOverlayOn(getContext())

        mHeaderTitle?.setText(R.string.header_title_grid_overlay)
        mHeaderSummary?.setText(R.string.header_summary_grid_overlay)

        backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorGridOverlayCardTint))


        val view = LayoutInflater.from(context).inflate(R.layout.grid_overlay_content, mCardContent, true)

        mIncludeKeylines = view.findViewById(R.id.include_keylines)
        mIncudeCustomGrid = view.findViewById(R.id.include_custom_grid_size)
        mColumnSizer = view.findViewById(R.id.column_sizer)
        mColumnSizer.setProgress((PreferenceUtils.GridPreferences.getGridColumnSize(getContext(), 8) - 4) / 2)
        mRowSizer = view.findViewById(R.id.row_sizer)
        mRowSizer.setProgress((PreferenceUtils.GridPreferences.getGridRowSize(getContext(), 8) - 4) / 2)
        mGridPreview = view.findViewById(R.id.grid_preview)
        mGridPreview.setColumnSize(PreferenceUtils.GridPreferences.getGridColumnSize(getContext(), 8))
        mGridPreview.setRowSize(PreferenceUtils.GridPreferences.getGridRowSize(getContext(), 8))

        val mSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val size = 4 + progress * 2
                if (seekBar === mColumnSizer) {
                    mGridPreview.columnSize = size
                    PreferenceUtils.GridPreferences.setGridColumnSize(getContext(), size)
                } else if (seekBar === mRowSizer) {
                    mGridPreview.rowSize = size
                    PreferenceUtils.GridPreferences.setGridRowSize(getContext(), size)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }

        mColumnSizer.setOnSeekBarChangeListener(mSeekBarChangeListener)
        mRowSizer.setOnSeekBarChangeListener(mSeekBarChangeListener)

        val mCheckChangedListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView === mIncludeKeylines) {
                PreferenceUtils.GridPreferences.setShowKeylines(getContext(), isChecked)
            } else if (buttonView === mIncudeCustomGrid) {
                PreferenceUtils.GridPreferences.setUseCustomGridSize(getContext(), isChecked)
                if (isChecked) {
                    PreferenceUtils.GridPreferences.setGridColumnSize(getContext(), mGridPreview.columnSize)
                    PreferenceUtils.GridPreferences.setGridRowSize(getContext(), mGridPreview.rowSize)
                }
                mColumnSizer.isEnabled = isChecked
                mRowSizer.isEnabled = isChecked
            }
        }

        mIncludeKeylines.setChecked(PreferenceUtils.GridPreferences.getShowKeylines(context, false))
        mIncludeKeylines.setOnCheckedChangeListener(mCheckChangedListener)

        setIncludeCustomGridLines(PreferenceUtils.GridPreferences.getUseCustomGridSize(context, false))
        mIncudeCustomGrid.setOnCheckedChangeListener(mCheckChangedListener)

        mRowSizer.setOnTouchListener({ v1, event ->
                                         when (event.getAction()) {
                                             MotionEvent.ACTION_DOWN -> v1.getParent().requestDisallowInterceptTouchEvent(true)
                                             MotionEvent.ACTION_UP -> v1.getParent().requestDisallowInterceptTouchEvent(false)
                                         }
                                         v1.onTouchEvent(event)
                                         true
                                     })

        mDualColorPicker = view.findViewById(R.id.color_picker)

        mDualColorPicker.setOnClickListener { v12 ->
            val fm = (context as Activity).fragmentManager
            val dualColorPickerDialog = DualColorPickerDialog()
            dualColorPickerDialog.show(fm, "color_picker_dialog")
        }

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (PreferenceUtils.GridPreferences.KEY_GRID_LINE_COLOR == key) {
            mDualColorPicker.primaryColor = ColorUtils.getGridLineColor(context)
        } else if (PreferenceUtils.GridPreferences.KEY_KEYLINE_COLOR == key) {
            mDualColorPicker.secondaryColor = ColorUtils.getKeylineColor(context)
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (isChecked == DesignerTools.gridOverlayOn(context)) {
            return
        }
        if (isChecked) {
            LaunchUtils.lauchGridOverlayOrPublishTile(context, if (PreferenceUtils.GridPreferences.getGridOverlayActive(context, false))
                OnOffTileState.STATE_ON
            else
                OnOffTileState.STATE_OFF)
        } else {
            LaunchUtils.cancelGridOverlayOrUnpublishTile(context)
        }
    }

    private fun setIncludeCustomGridLines(include: Boolean) {
        mIncudeCustomGrid.isChecked = include
        mColumnSizer.isEnabled = include
        mRowSizer.isEnabled = include
    }

}