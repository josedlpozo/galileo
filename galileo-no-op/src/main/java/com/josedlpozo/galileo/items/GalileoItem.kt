package com.josedlpozo.galileo.items

import android.view.View

interface GalileoItem {

    val view : View

    val name: String

    val icon: Int

    fun snapshot(): String

}

val emptyGalileoItem = object : GalileoItem {
    override val view: View = throw IllegalAccessException()
    override val name: String = "EMPTY"
    override val icon: Int = 0
    override fun snapshot(): String = ""
}