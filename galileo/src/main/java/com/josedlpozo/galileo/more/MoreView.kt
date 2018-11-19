package com.josedlpozo.galileo.more

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.config.ConfigRepository
import com.josedlpozo.galileo.items.GalileoItem

class MoreView @JvmOverloads constructor(context: Context,
                                        attr: AttributeSet? = null,
                                        defStyleAttr: Int = 0) : RecyclerView(context, attr, defStyleAttr), GalileoItem {

    init {
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL))

        val plugins = ConfigRepository.more.map { it(context) }
        adapter = MoreEventAdapter(plugins, {

        })
    }

    override val view: View = this

    override val name: String = "More"

    override val icon: Int = R.drawable.ic_more

    override fun snapshot(): String = ""
}