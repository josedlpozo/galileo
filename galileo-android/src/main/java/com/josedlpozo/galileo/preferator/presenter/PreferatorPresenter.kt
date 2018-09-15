package com.josedlpozo.galileo.preferator.presenter

import com.josedlpozo.galileo.preferator.data.PreferatorDataSource
import com.josedlpozo.galileo.preferator.model.PreferatorConfig
import com.josedlpozo.galileo.preferator.model.Preferences

internal class PreferatorPresenter(private val view: View, private val dataSource: PreferatorDataSource) {

    var config: PreferatorConfig = PreferatorConfig()
        set(value) {
            field = value
            start()
        }

    interface View {
        fun render(preferences: Preferences)
    }

    fun start() = dataSource.get(config).also(view::render)

    fun snapshot(): String = dataSource.get(config).items.joinToString("\n")

}