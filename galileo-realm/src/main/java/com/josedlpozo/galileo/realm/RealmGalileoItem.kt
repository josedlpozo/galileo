package com.josedlpozo.galileo.realm

import android.content.Context
import android.view.View
import com.josedlpozo.galileo.core.GalileoItem

class RealmGalileoItem(private val context: Context) : GalileoItem {

    private val view: RealmView by lazy {
        RealmView(
            context
        )
    }

    override val name: String = "RealmBrowser"

    override val icon: Int = R.drawable.realm_browser_ic_rb

    override fun snapshot(): String = view.snapshot()

    override fun view(): View = view
}