package com.josedlpozo.galileo.config

import com.josedlpozo.galileo.core.GalileoPlugin

internal data class GalileoInternalPlugin(val id: Long, val plugin: GalileoPlugin)

internal data class GalileoInternalConfig(val plugins: List<GalileoInternalPlugin> = listOf())