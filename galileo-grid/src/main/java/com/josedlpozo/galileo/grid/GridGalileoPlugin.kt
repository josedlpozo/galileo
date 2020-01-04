package com.josedlpozo.galileo.grid

import android.content.Context
import com.josedlpozo.galileo.core.GalileoFloatItem
import com.josedlpozo.galileo.core.GalileoFloatPlugin
import com.josedlpozo.galileo.core.GalileoItem
import com.josedlpozo.galileo.core.GalileoPlugin
import com.josedlpozo.galileo.grid.overlays.GridOverlay

val gridPlugin: GalileoPlugin = object : GalileoFloatPlugin() {

    override fun item(context: Context): GalileoItem = GridGalileoItem(context)

    override fun floatItem(): GalileoFloatItem = GridOverlay()

}