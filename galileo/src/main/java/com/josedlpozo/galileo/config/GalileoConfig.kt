package com.josedlpozo.galileo.config

import com.josedlpozo.galileo.chuck.ui.TransactionGalileoItem
import com.josedlpozo.galileo.config.GalileoOpenType.Floating
import com.josedlpozo.galileo.core.GalileoPlugin
import com.josedlpozo.galileo.flow.FlowGalileoItem
import com.josedlpozo.galileo.picker.GridGalileoItem
import com.josedlpozo.galileo.picker.PickerGalileoItem
import com.josedlpozo.galileo.preferator.view.PreferatorGalileoItem

class GalileoConfig internal constructor(
    val plugins: List<GalileoPlugin> = listOf(),
    val openType: GalileoOpenType
)

sealed class GalileoOpenType {
    object Floating : GalileoOpenType()
    object Shaking : GalileoOpenType()
    object Both : GalileoOpenType()
}

class GalileoConfigBuilder {

    private val plugins: MutableList<GalileoPlugin> = mutableListOf()
    private var openType: GalileoOpenType = Floating

    fun defaultPlugins(): GalileoConfigBuilder {
        plugins.addAll(defaultPlugins)
        return this
    }

    fun add(plugin: GalileoPlugin): GalileoConfigBuilder {
        plugins.add(plugin)
        return this
    }

    fun openType(openType: GalileoOpenType): GalileoConfigBuilder {
        this.openType = openType
        return this
    }

    fun build(): GalileoConfig = GalileoConfig(plugins, openType)
}

val defaultPlugins = listOf<GalileoPlugin>(
    { PreferatorGalileoItem(it) },
    { TransactionGalileoItem(it) },
    { FlowGalileoItem(it) },
    { PickerGalileoItem(it) },
    { GridGalileoItem(it) })