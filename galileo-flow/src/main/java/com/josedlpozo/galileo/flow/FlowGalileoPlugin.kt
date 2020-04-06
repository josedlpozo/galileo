package com.josedlpozo.galileo.flow

import android.app.Application
import android.content.Context
import com.josedlpozo.galileo.core.GalileoItem
import com.josedlpozo.galileo.core.GalileoPlugin

val flowPlugin: GalileoPlugin = object : GalileoPlugin() {

    override fun init(application: Application) {
        super.init(application)
        application.registerActivityLifecycleCallbacks(
            FlowEventTry.flowLifeCycleCallback
        )
    }

    override fun item(context: Context): GalileoItem = FlowGalileoItem(context)

}