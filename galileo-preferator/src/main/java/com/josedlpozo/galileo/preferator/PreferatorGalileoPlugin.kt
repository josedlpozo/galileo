package com.josedlpozo.galileo.preferator

import android.content.Context
import com.josedlpozo.galileo.core.GalileoItem
import com.josedlpozo.galileo.core.GalileoPlugin
import com.josedlpozo.galileo.preferator.view.PreferatorGalileoItem

val preferatorPlugin: GalileoPlugin = object : GalileoPlugin() {

    override fun item(context: Context): GalileoItem = PreferatorGalileoItem(context)

}