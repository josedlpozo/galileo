package com.josedlpozo.galileo.common

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.josedlpozo.galileo.core.GalileoFloatItem
import com.josedlpozo.galileo.floaticon.GalileoFloat

internal class GalileoApplicationLifeCycle(private val floats: List<GalileoFloatItem>) : Application.ActivityLifecycleCallbacks {

    private var activities = 0

    override fun onActivitySaveInstanceState(activity: Activity?, bundle: Bundle?) = Unit
    override fun onActivityStopped(activity: Activity?) = Unit
    override fun onActivityStarted(activity: Activity?) = Unit

    override fun onActivityPaused(activity: Activity?) {
        if (activity == null) return
        floats.filter { !(isGalileoActivity(activity) && it is GalileoFloat) }.map {
            it.onPaused()
        }
    }

    override fun onActivityResumed(activity: Activity?) {
        if (activity == null) return
        floats.filter { !(isGalileoActivity(activity) && it is GalileoFloat) }.map {
            it.onResume(activity)
        }
    }

    override fun onActivityDestroyed(activity: Activity?) = Unit

    override fun onActivityCreated(activity: Activity?, bundle: Bundle?) {
        if (activity == null) return
        activities += 1
        if (activities == 1) {
            floats.filter { !(isGalileoActivity(activity) && it is GalileoFloat) }.map { it.onCreate(activity) }
        }
    }

    private fun isGalileoActivity(activity: Activity): Boolean =
            "com.josedlpozo.galileo.*".toRegex().matches(activity.localClassName)

}