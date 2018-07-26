package com.sloydev.preferator.presenter

import com.sloydev.preferator.data.PreferatorDataSource
import com.sloydev.preferator.model.PreferatorConfig
import com.sloydev.preferator.model.Preferences

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