package com.josedlpozo.galileo.parent

import com.josedlpozo.galileo.items.GalileoItem

object SnapshotGenerator {

    private const val preSeparator = "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"

    fun generate(items: List<GalileoItem>): String = items.joinToString("\n\n\n") {
        val snapshot = it.snapshot()
        if (snapshot.isEmpty()) return@joinToString ""

        val name = "   ${it.name.toUpperCase()}   "
        "$preSeparator\n" +
                "$preSeparator\n" +
                "${nameDecorator(name)}$name${nameDecorator(name)}\n" +
                "$preSeparator\n" +
                "$preSeparator\n\n" + it.snapshot()
    }

    private fun nameDecorator(name: String): String =
            (0 until ((preSeparator.length - name.length) / 2)).joinToString(separator = "") { "%" }
}