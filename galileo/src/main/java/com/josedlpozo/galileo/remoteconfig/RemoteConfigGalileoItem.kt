package com.josedlpozo.galileo.remoteconfig

import android.content.Context
import android.view.View
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.core.GalileoItem

class RemoteConfigGalileoItem(private val context: Context) :
    GalileoItem {

    private val view: RemoteConfigView by lazy { RemoteConfigView(context) }

    override val name: String = "RemoteConfig"

    override val icon: Int = R.drawable.ic_developer_board

    override fun snapshot(): String = view.snapshot()

    override fun view(): View = view
}