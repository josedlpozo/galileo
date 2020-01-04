package com.josedlpozo.galileo.more

import android.content.Context
import com.josedlpozo.galileo.config.GalileoInternalPlugin
import com.josedlpozo.galileo.core.GalileoItem
import com.josedlpozo.galileo.core.GalileoPlugin

internal class MoreGalileoPlugin(private val plugins: List<GalileoInternalPlugin> = listOf()) : GalileoPlugin() {
    override fun item(context: Context): GalileoItem = MoreGalileoItem(plugins, context)
}