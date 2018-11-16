package com.josedlpozo.galileo.parent

import android.view.View
import com.josedlpozo.galileo.items.GalileoItem
import org.junit.Assert.assertEquals
import org.junit.Test

class SnapshotGeneratorTest {

    @Test
    fun `given a list with one GalileoItem, when calling generate, then Snapshot is correct`() {
        val result = SnapshotGenerator.generate(listOf(ITEM))

        val expected = """
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            %%%%%%%%%%%%%%%%%%%%   NAME   %%%%%%%%%%%%%%%%%%%%
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

            snapshot
        """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun `given a list with two GalileoItem, when calling generate, then Snapshot is correct`() {
        val result = SnapshotGenerator.generate(listOf(ITEM, ITEM))

        val expected = """
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            %%%%%%%%%%%%%%%%%%%%   NAME   %%%%%%%%%%%%%%%%%%%%
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

            snapshot


            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            %%%%%%%%%%%%%%%%%%%%   NAME   %%%%%%%%%%%%%%%%%%%%
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

            snapshot
        """.trimIndent()
        assertEquals(expected, result)
    }

    companion object {
        const val NAME = "name"
        const val SNAPSHOT = "snapshot"
        val ITEM = object : GalileoItem {
            override val galileoView: View
                get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
            override val name: String
                get() = NAME
            override val icon: Int
                get() = 0

            override fun snapshot(): String = SNAPSHOT

        }
    }
}