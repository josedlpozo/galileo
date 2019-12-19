package com.josedlpozo.galileo.core

import android.view.View

val emptyGalileoItem = object : GalileoItem {
    override fun view(): View = throw IllegalAccessException()
    override val name: String = "EMPTY"
    override val icon: Int = 0
    override fun snapshot(): String = ""
}