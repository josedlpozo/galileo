package com.josedlpozo.galileo.core

import android.view.View

interface GalileoItem {

    val name: String

    val icon: Int

    fun snapshot(): String

    fun view(): View

}