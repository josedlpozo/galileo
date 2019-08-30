package com.josedlpozo.galileo.more

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import com.josedlpozo.galileo.config.GalileoInternalPlugin
import com.josedlpozo.galileo.items.GalileoItem
import com.josedlpozo.galileo.parent.SnapshotGenerator

internal class MoreView @JvmOverloads internal constructor(plugins: List<GalileoInternalPlugin> = listOf(),
                                                  context: Context,
                                                  attr: AttributeSet? = null,
                                                  defStyleAttr: Int = 0) : RecyclerView(context, attr, defStyleAttr) {

    private val items: List<MoreItems> = plugins.map { MoreItems(it.id, it.plugin(context)) }

    init {
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL))

        adapter = MoreEventAdapter(items) {
            PluginActivity.start(context, it.id)
        }
    }

    fun snapshot(): String = SnapshotGenerator.generate(items.map { it.item })
}

data class MoreItems(val id: Long, val item: GalileoItem)