package com.josedlpozo.galileo.realm

import android.content.Context
import com.josedlpozo.galileo.core.GalileoItem
import com.josedlpozo.galileo.core.GalileoPlugin

val realmPlugin: GalileoPlugin = object : GalileoPlugin() {

    override fun item(context: Context): GalileoItem = RealmGalileoItem(context)

}