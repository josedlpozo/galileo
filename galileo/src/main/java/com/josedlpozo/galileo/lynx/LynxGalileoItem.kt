package com.josedlpozo.galileo.lynx

import android.content.Context
import android.view.View
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.items.GalileoItem

class LynxGalileoItem(private val context: Context) : GalileoItem {

    private val view: GalileoLynx by lazy { GalileoLynx(context) }

    override val name: String = "Lynx"

    override val icon: Int = R.drawable.ic_adb

    override fun snapshot(): String = view.snapshot()

    override fun view(): View = view
}