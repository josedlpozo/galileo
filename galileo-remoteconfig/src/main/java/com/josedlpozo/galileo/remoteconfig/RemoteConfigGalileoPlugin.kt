package com.josedlpozo.galileo.remoteconfig

import android.content.Context
import com.josedlpozo.galileo.core.GalileoItem
import com.josedlpozo.galileo.core.GalileoPlugin

val remoteConfigPlugin: GalileoPlugin = object : GalileoPlugin() {

    override fun item(context: Context): GalileoItem = RemoteConfigGalileoItem(context)

}