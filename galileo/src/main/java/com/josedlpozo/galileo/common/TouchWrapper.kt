package com.josedlpozo.galileo.common

import android.view.MotionEvent
import android.view.View

class TouchWrapper(var eventListener: OnTouchEventListener?) {
    private val MIN_DISTANCE_MOVE = 4

    private var mLastX: Int = 0
    private var mLastY: Int = 0
    private var mStartX: Int = 0
    private var mStartY: Int = 0
    private var mState = TouchState.STATE_STOP

    private enum class TouchState {
        STATE_MOVE,
        STATE_STOP
    }

    fun onTouchEvent(v: View, event: MotionEvent): Boolean {
        val x = event.rawX.toInt()
        val y = event.rawY.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = x
                mStartY = y
                mLastY = y
                mLastX = x
                eventListener?.onDown(x, y)
            }
            MotionEvent.ACTION_MOVE -> run {
                if (Math.abs(x - mStartX) < MIN_DISTANCE_MOVE && Math.abs(y - mStartY) < MIN_DISTANCE_MOVE) {
                    if (mState == TouchState.STATE_STOP) {
                        return@run
                    }
                } else if (mState != TouchState.STATE_MOVE) {
                    mState = TouchState.STATE_MOVE
                }
                eventListener?.onMove(mLastX, mLastY, x - mLastX, y - mLastY)
                mLastY = y
                mLastX = x
                mState = TouchState.STATE_MOVE
            }
            MotionEvent.ACTION_UP -> {
                eventListener?.onUp(x, y)
                if (mState != TouchState.STATE_MOVE) {
                    v.performClick()
                }
                mState = TouchState.STATE_STOP
            }
            else -> {
            }
        }
        return true
    }


    interface OnTouchEventListener {
        fun onMove(x: Int, y: Int, dx: Int, dy: Int)

        fun onUp(x: Int, y: Int)

        fun onDown(x: Int, y: Int)
    }
}