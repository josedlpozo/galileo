package com.josedlpozo.galileo.parent.preparator

import android.content.Context
import com.josedlpozo.galileo.config.ConfigRepository
import com.josedlpozo.galileo.config.GalileoConfig
import com.josedlpozo.galileo.config.GalileoInternalConfig
import com.josedlpozo.galileo.config.GalileoOpenType
import com.josedlpozo.galileo.core.GalileoItem
import com.josedlpozo.galileo.core.GalileoPlugin
import com.josedlpozo.galileo.core.emptyGalileoItem
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PluginsPreparatorTest {

    @Before
    fun setup() {
        ConfigRepository.internalConfig = GalileoInternalConfig()
        ConfigRepository.more = listOf()
    }

    @Test
    fun `given one plugin, when preparing, then config will have one plugin and more will have zero plugins`() {
        PluginsPreparator.prepare(
            GalileoConfig(
                listOf(plugin),
                GalileoOpenType.Floating
            )
        )

        assertEquals(1, ConfigRepository.internalConfig.plugins.size)
        assertEquals(0, ConfigRepository.more.size)
    }

    @Test
    fun `given five plugins, when preparing, then config will have five plugins and more will have zero plugins`() {
        val plugins = listOf(plugin, plugin, plugin, plugin, plugin)
        PluginsPreparator.prepare(GalileoConfig(plugins, GalileoOpenType.Floating))

        assertEquals(5, ConfigRepository.internalConfig.plugins.size)
        assertEquals(0, ConfigRepository.more.size)
    }

    @Test
    fun `given six plugins, when preparing, then config will have five plugins and more will have two plugins`() {
        val plugins = listOf(plugin, plugin, plugin, plugin, plugin, plugin)
        PluginsPreparator.prepare(GalileoConfig(plugins, GalileoOpenType.Floating))

        assertEquals(5, ConfigRepository.internalConfig.plugins.size)
        assertEquals(2, ConfigRepository.more.size)
    }

    private val plugin: GalileoPlugin = object : GalileoPlugin() {
        override fun item(context: Context): GalileoItem = emptyGalileoItem
    }
}