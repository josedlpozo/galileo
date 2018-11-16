package com.josedlpozo.galileo.parent.extensions

import android.graphics.drawable.Drawable
import android.support.annotation.DimenRes
import android.view.View

fun Drawable.tint(color: Int) : Drawable {
    mutate()
    setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_ATOP)
    setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    return this
}

fun View.padding(@DimenRes dimen: Int) {
    val padding = context.resources.getDimension(dimen).toInt()
    setPadding(padding, padding, padding, padding)
}