package com.josedlpozo.galileo.flow

import com.josedlpozo.galileo.flow.model.Created
import com.josedlpozo.galileo.flow.model.CreatedModel
import com.josedlpozo.galileo.flow.model.Destroyed
import com.josedlpozo.galileo.flow.model.DestroyedModel
import com.josedlpozo.galileo.flow.model.Resumed
import com.josedlpozo.galileo.flow.model.ResumedModel
import com.josedlpozo.galileo.flow.model.toModel
import org.junit.Assert.assertTrue
import org.junit.Test

class FlowEventModelMapperTest {

    @Test
    fun `given a Resumed FlowEvent, when mapped, then return a ResumedModel FlowEventModel`() {
        assertTrue(Resumed(123, "", 0).toModel() is ResumedModel)
    }

    @Test
    fun `given a Destroy FlowEvent, when mapped, then return a DestroyedModel FlowEventModel`() {
        assertTrue(Destroyed(123, "", 0).toModel() is DestroyedModel)
    }

    @Test
    fun `given a Created FlowEvent, when mapped, then return a CreatedModel FlowEventModel`() {
        assertTrue(Created(123, "", 0, listOf()).toModel() is CreatedModel)
    }
}