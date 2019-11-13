package com.josedlpozo.galileo.more

import android.content.Context
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import com.josedlpozo.galileo.config.GalileoInternalPlugin
import com.josedlpozo.galileo.items.GalileoItem
import com.josedlpozo.galileo.parent.SnapshotGenerator

internal class MoreView @JvmOverloads internal constructor(plugins: List<GalileoInternalPlugin> = listOf(),
                                                  context: Context,
                                                  attr: AttributeSet? = null,
                                                  defStyleAttr: Int = 0) : androidx.recyclerview.widget.RecyclerView(context, attr, defStyleAttr) {

    private val items: List<MoreItems> = plugins.map { MoreItems(it.id, it.plugin(context)) }

    init {
        layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                getContext(),
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
        )

        adapter = MoreEventAdapter(items) {
            PluginActivity.start(context, it.id)
        }
    }

    fun snapshot(): String = SnapshotGenerator.generate(items.map { it.item })
}

data class MoreItems(val id: Long, val item: GalileoItem)