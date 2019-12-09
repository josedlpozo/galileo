package com.josedlpozo.galileo.picker

import android.content.Context
import android.view.View
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.core.GalileoItem

class GridGalileoItem(private val context: Context) :
    GalileoItem {

    private val view: View by lazy { GridView(context) }

    override val name: String = "Grid"

    override val icon: Int = R.drawable.ic_qs_grid_on

    override fun snapshot(): String = ""

    override fun view(): View = view
}