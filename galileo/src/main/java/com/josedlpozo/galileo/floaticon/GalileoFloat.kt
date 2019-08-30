package com.josedlpozo.galileo.floaticon

import android.app.Activity
import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.common.BaseFloatItem
import com.josedlpozo.galileo.common.TouchWrapper
import com.josedlpozo.galileo.common.TouchWrapper.OnTouchEventListener
import com.josedlpozo.galileo.parent.extensions.show

internal class GalileoFloat(private val click: () -> Unit) : BaseFloatItem(), OnTouchEventListener {

    private lateinit var ivIconGalileo: AppCompatImageView
    private val mTouchProxy = TouchWrapper(this)

    override fun onViewCreated(view: View) {
        view.setOnClickListener { click.invoke() }
        view.setOnTouchListener { v, event -> mTouchProxy.onTouchEvent(v, event) }
    }

    override fun onCreate(context: Context) {
        view = LayoutInflater.from(context).inflate(R.layout.galileo_float, null, false)
        ivIconGalileo = view.findViewById(R.id.ivIconGalileo)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.x = FloatIconPreferenceUtils.getX(view.context)
        view.y = FloatIconPreferenceUtils.getY(view.context)
        super.onCreate(context)
    }

    override fun onResume(activity: Activity) {
        super.onResume(activity)
        view.show()
        ivIconGalileo.show()
    }

    override fun onMove(x: Int, y: Int, dx: Int, dy: Int) {
        view.x += dx
        view.y += dy
        FloatIconPreferenceUtils.setX(view.context, view.x)
        FloatIconPreferenceUtils.setY(view.context, view.y)
    }
}