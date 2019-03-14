package com.josedlpozo.galileo.common

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import com.josedlpozo.galileo.parent.home.HomeActivity

class GalileoApplicationLifeCycle(private val context: Context, private val onCreate:() -> Unit,
                                  private val onDestroy: () -> Unit) : Application.ActivityLifecycleCallbacks {

    private val galileoFloat = GalileoFloat {
        Intent(context, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }.also {
            context.startActivity(it)
        }
    }

    var activities = 0

    override fun onActivityPaused(activity: Activity?) = Unit
    override fun onActivityResumed(activity: Activity?) {
        galileoFloat.performCreate(activity!!)
        activity.window.addContentView(galileoFloat.rootView!!, galileoFloat.layoutParams)
    }
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