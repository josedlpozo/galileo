package com.josedlpozo.galileo.core

import android.app.Application
import android.content.Context

abstract class GalileoPlugin {

    open fun init(application: Application) = Unit

    abstract fun item(context: Context): GalileoItem
}

abstract class GalileoFloatPlugin : GalileoPlugin() {

    abstract fun floatItem(): GalileoFloatItem

}