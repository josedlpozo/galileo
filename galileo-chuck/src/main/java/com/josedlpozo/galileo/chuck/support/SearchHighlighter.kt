package com.josedlpozo.galileo.chuck.support

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan


object SearchHighlighter {

    fun format(text: String, criteria: String): SpannableStringBuilder {
        val startIndexes = indexesOf(text, criteria)
        return applySpannable(text, startIndexes, criteria.length)
    }

    private fun indexesOf(text: String, criteria: String): List<Int> {
        val startPositions = mutableListOf<Int>()
        var index = text.indexOf(criteria)
        if (index == -1) return startPositions
        do {
            startPositions.add(index)
            index = text.indexOf(criteria, index + 1)
        } while (index >= 0)
        return startPositions
    }

    private fun applySpannable(text: String, indexes: List<Int>, length: Int): SpannableStringBuilder {
        val builder = SpannableStringBuilder(text)
        for (position in indexes) {
            builder.setSpan(BackgroundColorSpan(Color.YELLOW), position,
                    position + length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return builder
    }

}