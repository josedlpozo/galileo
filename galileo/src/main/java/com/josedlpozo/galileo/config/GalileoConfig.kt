package com.josedlpozo.galileo.config

import android.content.Context
import com.josedlpozo.galileo.activities.GalileoActivitiesLifeCycle
import com.josedlpozo.galileo.chuck.internal.ui.TransactionListView
import com.josedlpozo.galileo.items.GalileoItem
import com.josedlpozo.galileo.lynx.GalileoLynx
import com.josedlpozo.galileo.preferator.Preferator

typealias GalileoPlugin = (Context) -> GalileoItem

class GalileoConfig internal constructor(val plugins: List<GalileoPlugin> = listOf())

class GalileoConfigBuilder {

    private val plugins: MutableList<GalileoPlugin> = mutableListOf()

    fun defaultPlugins() : GalileoConfigBuilder {
        plugins.addAll(defaultPlugins)
        return this
    }

    fun add(plugin: GalileoPlugin) : GalileoConfigBuilder {
        plugins.add(plugin)
        return this
    }

    fun build() : GalileoConfig = GalileoConfig(plugins)
}

val defaultPlugins = listOf<GalileoPlugin>({ Preferator.view(it) }, { GalileoLynx(it) },
        { TransactionListView(it) }, { GalileoActivitiesLifeCycle(it) })