package com.josedlpozo.galileo.more

import android.content.Context
import android.view.View
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.config.GalileoInternalPlugin
import com.josedlpozo.galileo.core.GalileoItem

internal class MoreGalileoItem(private val plugins: List<GalileoInternalPlugin> = listOf(), private val context: Context) :
    GalileoItem {

    private val view: MoreView by lazy { MoreView(plugins, context) }

    override val name: String = "More"

    override val icon: Int = R.drawable.ic_more

    override fun snapshot(): String = view.snapshot()

    override fun view(): View = view
}