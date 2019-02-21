package com.josedlpozo.galileo.common

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.josedlpozo.galileo.parent.home.HomeActivity

class GalileoFloatLifeCycle(private val galileoFloat: GalileoFloat) : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity?) = Unit
    override fun onActivityResumed(activity: Activity?) = Unit
    override fun onActivitySaveInstanceState(activity: Activity?, bundle: Bundle?) = Unit
    override fun onActivityStopped(activity: Activity?) = Unit
    override fun onActivityStarted(activity: Activity?) = Unit

    override fun onActivityDestroyed(activity: Activity?) {
        if (activity is HomeActivity) {
            galileoFloat.show()
        }
    }

    override fun onActivityCreated(activity: Activity?, bundle: Bundle?) {
        if (activity is HomeActivity) {
            galileoFloat.hide()
        }
    }

}