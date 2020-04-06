package com.josedlpozo.galileo.colorpicker

import android.content.Context
import com.josedlpozo.galileo.colorpicker.overlay.ColorPickerOverlay
import com.josedlpozo.galileo.core.GalileoFloatItem
import com.josedlpozo.galileo.core.GalileoFloatPlugin
import com.josedlpozo.galileo.core.GalileoItem

val colorPickerPlugin: GalileoFloatPlugin = object : GalileoFloatPlugin() {

    override fun item(context: Context): GalileoItem = PickerGalileoItem(context)

    override fun floatItem(): GalileoFloatItem = ColorPickerOverlay()

}