package com.josedlpozo.galileo.floaticon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.common.BaseFloatItem
import com.josedlpozo.galileo.common.TouchWrapper
import com.josedlpozo.galileo.common.TouchWrapper.OnTouchEventListener

class GalileoFloat(private val click: () -> Unit) : BaseFloatItem(), OnTouchEventListener {

    private val mTouchProxy = TouchWrapper(this)

    override fun onViewCreated(view: View) {
        view.setOnClickListener { click.invoke() }
        view.setOnTouchListener { v, event -> mTouchProxy.onTouchEvent(v, event) }
    }

    override fun onCreate(context: Context) {
        view = LayoutInflater.from(context).inflate(R.layout.galileo_float, null, false)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        super.onCreate(context)
    }

    override fun onEnterForeground() {
        super.onEnterForeground()
        show()
    }

    override fun onEnterBackground() {
        super.onEnterBackground()
        hide()
    }

    override fun onMove(x: Int, y: Int, dx: Int, dy: Int) {
        view.x += dx
        view.y += dy
        FloatIconPreferenceUtils.setX(view.context, view.x)
        FloatIconPreferenceUtils.setY(view.context, view.y)
    }
}