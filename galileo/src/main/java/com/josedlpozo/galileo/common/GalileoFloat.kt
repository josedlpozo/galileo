package com.josedlpozo.galileo.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.parent.extensions.hide
import com.josedlpozo.galileo.parent.extensions.show

class GalileoFloat(private val click: () -> Unit) : BaseFloatItem(), TouchWrapper.OnTouchEventListener {

    private val mTouchProxy = TouchWrapper(this)

    override fun onViewCreated(view: View) {
        rootView?.setOnClickListener { click.invoke() }
        rootView?.setOnTouchListener { v, event -> mTouchProxy.onTouchEvent(v, event) }
    }

    override fun onCreateView(context: Context): View =
            LayoutInflater.from(context).inflate(R.layout.galileo_float, null, false)

    override fun onLayoutParamsCreated(params: WindowManager.LayoutParams) {
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
    }

    override fun onCreate(context: Context) {}

    override fun onEnterForeground() {
        super.onEnterForeground()
        show()
    }

    override fun onEnterBackground() {
        super.onEnterBackground()
        hide()
    }

    override fun onMove(x: Int, y: Int, dx: Int, dy: Int) {
        rootView?.x = rootView?.x?.plus(dx) ?: return
        rootView?.y = rootView?.y?.plus(dy) ?: return
        //windowManager.updateViewLayout(rootView, layoutParams)
    }
}