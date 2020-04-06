package com.josedlpozo.galileo.lynx

import android.content.Context
import android.view.View
import android.widget.ImageButton
import com.github.pedrovgs.lynx.LynxConfig
import com.github.pedrovgs.lynx.LynxView
import com.github.pedrovgs.lynx.model.Trace
import com.github.pedrovgs.lynx.model.TraceLevel.ERROR

internal class GalileoLynx(context: Context) : LynxView(context) {

    private val oldTraces: MutableList<Trace> = mutableListOf()

    private val shareButton: ImageButton by lazy { findViewById<View>(R.id.ib_share) as ImageButton }

    init {
        lynxConfig = LynxConfig().apply {
            filterTraceLevel = ERROR
            samplingRate = 200
        }

        shareButton.visibility = View.GONE
    }

    override fun showTraces(traces: MutableList<Trace>?, removedTraces: Int) {
        super.showTraces(traces, removedTraces)
        traces?.let {
            oldTraces.clear()
            oldTraces.addAll(it)
        }
    }

    fun snapshot(): String = oldTraces.joinToString("\n")

}