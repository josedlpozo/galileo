package com.josedlpozo.galileo.flow

import arrow.core.fix
import com.josedlpozo.galileo.flow.model.Resumed
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class FlowEventDataSourceTest {

    @Test
    fun `given an event to save, when saving, then is saved successfully`() {
        val event = Resumed(1234, "")
        FlowEventTry.dataSource.add(event)

        FlowEventTry.useCase.get().fix().fold({ fail() }, {
            assertEquals(listOf(event), it)
        })
    }

    @Test
    fun `given no events saved, when finding by id, then Try NoSuchElementException`() {
        FlowEventTry.useCase.get(1234).fix().fold({
                                                          assertTrue(it is NoSuchElementException)
                                                      }, { fail() })
    }
}