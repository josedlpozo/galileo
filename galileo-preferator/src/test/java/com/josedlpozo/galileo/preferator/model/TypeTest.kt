package com.josedlpozo.galileo.preferator.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.IllegalStateException

class TypeTest {

    @Test
    fun `given String, when calling of, then return String Type`() {
        val result = Type.of("")

        assertEquals(Type.STRING, result)
    }

    @Test
    fun `given Int, when calling of, then return Int Type`() {
        val result = Type.of(0)

        assertEquals(Type.INT, result)
    }

    @Test
    fun `given Long, when calling of, then return Long Type`() {
        val result = Type.of(0.toLong())

        assertEquals(Type.LONG, result)
    }

    @Test
    fun `given Float, when calling of, then return Float Type`() {
        val result = Type.of(0.toFloat())

        assertEquals(Type.FLOAT, result)
    }

    @Test
    fun `given Boolean, when calling of, then return Boolean Type`() {
        val result = Type.of(false)

        assertEquals(Type.BOOLEAN, result)
    }

    @Test
    fun `given Set, when calling of, then return Set Type`() {
        val result = Type.of(emptySet<String>())

        assertEquals(Type.SET, result)
    }

    @Test(expected = IllegalStateException::class)
    fun `given null, when calling of, then throws IllegalStateException`() {
        Type.of(null)
    }

    @Test(expected = IllegalStateException::class)
    fun `given a List, when calling of, then throws IllegalStateException`() {
        Type.of(listOf<String>())
    }
}