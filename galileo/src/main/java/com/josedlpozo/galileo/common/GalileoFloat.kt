package com.josedlpozo.galileo.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.josedlpozo.galileo.R

class GalileoFloat : FloatItem(), TouchWrapper.OnTouchEventListener {

    private lateinit var mWindowManager: WindowManager

    private val mTouchProxy = TouchWrapper(this)

    override fun onViewCreated(view: View?) {
        getRootView()?.setOnClickListener {
            /*val pageIntent = PageIntent(KitFloatPage::class.java)
                pageIntent.mode = PageIntent.MODE_SINGLE_INSTANCE
                FloatPageManager.getInstance().add(pageIntent)*/
        }
        getRootView()?.setOnTouchListener { v, event -> mTouchProxy.onTouchEvent(v, event) }
    }

    override fun onCreateView(context: Context, view: ViewGroup?): View = LayoutInflater.from(context).inflate(R.layout.galileo_float, view, false)

    override fun onLayoutParamsCreated(params: WindowManager.LayoutParams) {
        /*params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        params.x = FloatIconConfig.getLastPosX(getContext())
        params.y = FloatIconConfig.getLastPosY(getContext())
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT*/
    }

    override fun onCreate(context: Context) {
        //mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    /*fun onEnterForeground() {
        super.onEnterForeground()
        getRootView().setVisibility(View.VISIBLE)
    }

    fun onEnterBackground() {
        super.onEnterBackground()
        getRootView().setVisibility(View.GONE)
    }*/

    override fun onMove(x: Int, y: Int, dx: Int, dy: Int) {
        getLayoutParams().x += dx
        getLayoutParams().y += dy
        mWindowManager.updateViewLayout(getRootView(), getLayoutParams())
    }

    override fun onUp(x: Int, y: Int) {
        //FloatIconConfig.saveLastPosX(getContext(), getLayoutParams().x)
        //FloatIconConfig.saveLastPosY(getContext(), getLayoutParams().y)
    }

    override fun onDown(x: Int, y: Int) {

    }
}