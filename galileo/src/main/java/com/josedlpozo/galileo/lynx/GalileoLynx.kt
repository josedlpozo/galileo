package com.josedlpozo.galileo.lynx

import android.content.Context
import android.view.View
import android.widget.ImageButton
import com.github.pedrovgs.lynx.LynxConfig
import com.github.pedrovgs.lynx.LynxView
import com.github.pedrovgs.lynx.model.Trace
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.items.GalileoItem

internal class GalileoLynx(context: Context) : LynxView(context), GalileoItem {

    private val oldTraces: MutableList<Trace> = mutableListOf()

    private val shareButton: ImageButton by lazy { findViewById<View>(R.id.ib_share) as ImageButton }

    init {
        lynxConfig = LynxConfig().apply {
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

    override val name: String = "Lynx"

    override val icon: Int = R.drawable.ic_adb

    override val galileoView: View = this

    override fun snapshot(): String = oldTraces.joinToString("\n")

}