package com.josedlpozo.galileo.lynx

import android.content.Context
import android.view.View
import com.github.pedrovgs.lynx.LynxConfig
import com.github.pedrovgs.lynx.LynxView
import com.github.pedrovgs.lynx.model.Trace
import com.josedlpozo.galileo.items.GalileoItem

class GalileoLynx(context: Context) : LynxView(context), GalileoItem {

    private val oldTraces: MutableList<Trace> = mutableListOf()

    init {
        lynxConfig = LynxConfig().apply {
            filter = getApplicationName()
        }
    }

    override fun showTraces(traces: MutableList<Trace>?, removedTraces: Int) {
        super.showTraces(traces, removedTraces)
        traces?.let {
            oldTraces.clear()
            oldTraces.addAll(it)
        }
    }

    private fun getApplicationName(): String {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(stringId)
    }

    override val name: String
        get() = "Lynx"

    override val icon: Int
        get() = android.R.drawable.stat_sys_warning

    override val view: View
        get() = this

    override fun snapshot(): String = oldTraces.joinToString("\n")

}