package com.josedlpozo.galileo.preferator.view

import android.content.Context
import android.view.View
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.items.GalileoItem
import com.josedlpozo.galileo.preferator.model.PreferatorConfig

class PreferatorGalileoItem(private val context: Context) : GalileoItem {

    private val view: PreferatorView by lazy { PreferatorView(context).apply { config = PreferatorConfig() } }

    override val name: String = "Preferator"

    override val icon: Int = R.drawable.ic_developer_board

    override fun snapshot(): String = view.snapshot()

    override fun view(): View = view
}