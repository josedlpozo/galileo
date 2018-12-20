package com.josedlpozo.galileo.config

import android.content.Context
import com.josedlpozo.galileo.chuck.ui.TransactionListView
import com.josedlpozo.galileo.flow.FlowView
import com.josedlpozo.galileo.items.GalileoItem
import com.josedlpozo.galileo.lynx.GalileoLynx
import com.josedlpozo.galileo.picker.GridView
import com.josedlpozo.galileo.picker.PickerView
import com.josedlpozo.galileo.preferator.Preferator
import com.josedlpozo.galileo.realm.RealmView

typealias GalileoPlugin = (Context) -> GalileoItem

class GalileoConfig internal constructor(val plugins: List<GalileoPlugin> = listOf())

class GalileoConfigBuilder {

    private val plugins: MutableList<GalileoPlugin> = mutableListOf()

    fun defaultPlugins(): GalileoConfigBuilder {
        plugins.addAll(defaultPlugins)
        return this
    }

    fun add(plugin: GalileoPlugin): GalileoConfigBuilder {
        plugins.add(plugin)
        return this
    }

    fun build(): GalileoConfig = GalileoConfig(plugins)
}

val defaultPlugins = listOf<GalileoPlugin>({ Preferator.view(it) },
                                           { GalileoLynx(it) },
                                           { TransactionListView(it) },
                                           { FlowView(it) },
                                           { RealmView(it) },
                                           { PickerView(it) },
                                           { GridView(it) })