package com.josedlpozo.galileo.config

import com.josedlpozo.galileo.config.GalileoOpenType.Floating
import com.josedlpozo.galileo.core.GalileoPlugin

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