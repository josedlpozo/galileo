package com.josedlpozo.galileo.parent.preparator

import com.josedlpozo.galileo.config.ConfigRepository
import com.josedlpozo.galileo.config.GalileoConfig
import com.josedlpozo.galileo.config.GalileoInternalConfig
import com.josedlpozo.galileo.config.GalileoInternalPlugin
import com.josedlpozo.galileo.more.MoreView

object PluginsPreparator {
    private const val MAX_ITEMS = 5
    private const val MAX_SIZE_LIST = MAX_ITEMS - 1

    fun prepare(config: GalileoConfig) {
        ConfigRepository.internalConfig = if (config.plugins.size > MAX_ITEMS) {
            val first = config.plugins.take(MAX_SIZE_LIST).map { GalileoInternalPlugin(System.nanoTime(), it) }
            val more = config.plugins.drop(MAX_SIZE_LIST).map { GalileoInternalPlugin(System.nanoTime(), it) }
            ConfigRepository.more = more
            GalileoInternalConfig(first + GalileoInternalPlugin(System.nanoTime()) { MoreView(more, it) })
        } else GalileoInternalConfig(config.plugins.map { GalileoInternalPlugin(System.nanoTime(), it) })
    }
}