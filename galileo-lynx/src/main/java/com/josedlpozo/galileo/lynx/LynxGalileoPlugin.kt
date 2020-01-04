package com.josedlpozo.galileo.lynx

import android.content.Context
import com.josedlpozo.galileo.core.GalileoItem
import com.josedlpozo.galileo.core.GalileoPlugin

val lynxPlugin: GalileoPlugin = object : GalileoPlugin() {

    override fun item(context: Context): GalileoItem = LynxGalileoItem(context)

}