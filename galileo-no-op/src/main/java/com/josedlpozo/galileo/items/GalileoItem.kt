package com.josedlpozo.galileo.items

import android.view.View

interface GalileoItem {

    val name: String

    val icon: Int

    fun snapshot(): String

    fun view(): View

}

val emptyGalileoItem = object : GalileoItem {
    override fun view(): View = throw IllegalAccessException()
    override val name: String = "EMPTY"
    override val icon: Int = 0
    override fun snapshot(): String = ""
}