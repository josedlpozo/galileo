package com.josedlpozo.galileo.config

import com.josedlpozo.galileo.more.MoreView

internal object ConfigRepository {

    var config: GalileoConfig = GalileoConfig(listOf())
        set(value) {
            field = if (value.plugins.size > 5) {
                val first = value.plugins.take(4)
                val rest = value.plugins.drop(4)
                more = rest
                GalileoConfig(first + { MoreView(it) })
            } else value
        }

    var more: List<GalileoPlugin> = listOf()

}