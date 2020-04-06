package com.josedlpozo.galileo.config

internal object ConfigRepository {

    var internalConfig: GalileoInternalConfig = GalileoInternalConfig()

    var more: List<GalileoInternalPlugin> = listOf()

}