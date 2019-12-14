package com.josedlpozo.galileo.flow

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import androidx.annotation.DimenRes

internal fun Drawable.tint(color: Int): Drawable {
    mutate()
    setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_ATOP)
    setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    return this
}

internal fun View.padding(@DimenRes dimen: Int) {
    val padding = context.resources.getDimension(dimen).toInt()
    setPadding(padding, padding, padding, padding)
}

internal fun SpannableStringBuilder.toBold(text: String): SpannableStringBuilder {
    val start = this.indexOf(text)
    if (start == -1) return this
    setSpan(
        StyleSpan(Typeface.BOLD),
        start,
        start + text.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return this
}