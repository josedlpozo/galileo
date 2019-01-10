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
import com.josedlpozo.galileo.screenshooter.ScreenshooterView

typealias GalileoPlugin = (Context) -> GalileoItem

class GalileoConfig internal constructor(val plugins: List<GalileoPlugin> = listOf()) {

    fun screenshooter() = screenshooter

}

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

private val screenshooter: GalileoPlugin = { ScreenshooterView(it) }

private val defaultPlugins = listOf(
    { GalileoLynx(it) },
    { Preferator.view(it) },
    { TransactionListView(it) },
    { FlowView(it) },
    { RealmView(it) },
    { PickerView(it) },
    { GridView(it) },
    screenshooter)