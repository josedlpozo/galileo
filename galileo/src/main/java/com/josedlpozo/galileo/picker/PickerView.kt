package com.josedlpozo.galileo.picker

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import com.josedlpozo.galileo.items.GalileoItem
import com.josedlpozo.galileo.picker.qs.OnOffTileState
import com.josedlpozo.galileo.picker.ui.DesignerTools
import com.josedlpozo.galileo.picker.utils.LaunchUtils
import com.josedlpozo.galileo.picker.utils.PreferenceUtils
import com.josedlpozo.galileo.R

internal class PickerView @JvmOverloads internal constructor(context: Context) : LinearLayout(context), GalileoItem, CompoundButton.OnCheckedChangeListener {

    override val name: String = "Picker"
    override val galileoView: View = this
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
        backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorColorPickerCardTint))

        val mHeaderTitle = findViewById<TextView>(R.id.header_title)
        val mHeaderSummary = findViewById<TextView>(R.id.header_summary)
        swColorPicker = findViewById(R.id.enable_switch)
        swColorPicker.setOnCheckedChangeListener(this)
        swColorPicker.isChecked = DesignerTools.colorPickerOn(context)

        mHeaderTitle?.setText(R.string.header_title_color_picker)
        mHeaderSummary?.setText(R.string.header_summary_color_picker)
    }

    fun enableFeature(enable: Boolean) {
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