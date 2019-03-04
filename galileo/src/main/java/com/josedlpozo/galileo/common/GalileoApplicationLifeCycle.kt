package com.josedlpozo.galileo.common

import android.app.Activity
import android.app.Application
import android.os.Bundle

class GalileoApplicationLifeCycle(private val onCreate:() -> Unit,
                                  private val onDestroy: () -> Unit) : Application.ActivityLifecycleCallbacks {

    var activities = 0

    override fun onActivityPaused(activity: Activity?) = Unit
    override fun onActivityResumed(activity: Activity?) = Unit
    override fun onActivitySaveInstanceState(activity: Activity?, bundle: Bundle?) = Unit
    override fun onActivityStopped(activity: Activity?) = Unit
    override fun onActivityStarted(activity: Activity?) = Unit

    override fun onActivityDestroyed(activity: Activity?) {
        activities -= 1
        if (activities <= 0) onDestroy()
    }

    override fun onActivityCreated(activity: Activity?, bundle: Bundle?) {
        activities += 1
        if (activities == 1) onCreate()
    }

}