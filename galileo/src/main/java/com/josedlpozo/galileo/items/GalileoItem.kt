package com.josedlpozo.galileo.items

import android.view.View

interface GalileoItem {

    val galileoView : View

    val name: String

    val icon: Int

    fun snapshot(): String

}