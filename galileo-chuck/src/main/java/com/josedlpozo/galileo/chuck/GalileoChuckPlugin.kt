package com.josedlpozo.galileo.chuck

import android.content.Context
import com.josedlpozo.galileo.chuck.ui.TransactionGalileoItem
import com.josedlpozo.galileo.core.GalileoItem
import com.josedlpozo.galileo.core.GalileoPlugin

val chuckPlugin: GalileoPlugin = object : GalileoPlugin() {

    override fun item(context: Context): GalileoItem = TransactionGalileoItem(context)

}