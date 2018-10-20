package com.josedlpozo.galileo.preferator

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SdkFilterTest {

    @Test
    fun `given chuck_preferences as name, when calling isSdkPreference, then return true`() {
        assertTrue(SdkFilter.isSdkPreference("chuck_preferences"))
    }

    @Test
    fun `given com josedlpozo galileo sample as name, when calling isSdkPreference, then return false`() {
        assertFalse(SdkFilter.isSdkPreference("com.josedlpozo.galileo.sample"))
    }
}