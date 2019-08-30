/*
 * Copyright (C) 2018 josedlpozo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.josedlpozo.galileo.parent

import com.josedlpozo.galileo.items.GalileoItem

internal object SnapshotGenerator {

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