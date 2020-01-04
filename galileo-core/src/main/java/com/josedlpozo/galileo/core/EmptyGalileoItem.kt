package com.josedlpozo.galileo.core

import android.content.Context
import android.view.View

val emptyGalileoItem = object : GalileoItem {
    override fun view(): View = throw IllegalAccessException()
    override val name: String = "EMPTY"
    override val icon: Int = 0
    override fun snapshot(): String = ""
}

class EmptyGalileoPlugin : GalileoPlugin() {
    override fun item(context: Context): GalileoItem = emptyGalileoItem
}