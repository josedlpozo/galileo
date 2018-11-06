package com.josedlpozo.galileo.config

import android.content.Context
import com.josedlpozo.galileo.items.GalileoItem

typealias GalileoPlugin = (Context) -> GalileoItem

class GalileoConfig internal constructor(val plugins: List<GalileoPlugin> = listOf())

class GalileoConfigBuilder {

    private val plugins: MutableList<GalileoPlugin> = mutableListOf()

    fun defaultPlugins(): GalileoConfigBuilder = this

    fun add(plugin: GalileoPlugin): GalileoConfigBuilder = this

    fun build(): GalileoConfig = GalileoConfig(plugins)
}

val defaultPlugins = emptyList<GalileoPlugin>()