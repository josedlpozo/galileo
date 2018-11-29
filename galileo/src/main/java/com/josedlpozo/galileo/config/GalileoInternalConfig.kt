package com.josedlpozo.galileo.config

internal data class GalileoInternalPlugin(val id: Long, val plugin: GalileoPlugin)

internal data class GalileoInternalConfig(val plugins: List<GalileoInternalPlugin> = listOf())