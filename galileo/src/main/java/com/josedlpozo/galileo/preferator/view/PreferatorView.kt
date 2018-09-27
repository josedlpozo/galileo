package com.josedlpozo.galileo.preferator.view

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.items.GalileoItem
import com.josedlpozo.galileo.preferator.data.PreferatorDataSource
import com.josedlpozo.galileo.preferator.model.PreferatorConfig
import com.josedlpozo.galileo.preferator.model.Preferences
import com.josedlpozo.galileo.preferator.presenter.PreferatorPresenter
import com.josedlpozo.galileo.preferator.view.adapter.PreferatorAdapter

class PreferatorView @JvmOverloads internal constructor(context: Context, val attr: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attr, defStyleAttr), PreferatorPresenter.View, GalileoItem {

    override val name: String = "Preferator"

    override val view: View = this

    override val icon: Int = R.drawable.ic_developer_board

    override fun snapshot(): String = presenter.snapshot()

    private val recycler: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recycler) }
    private val adapter: PreferatorAdapter by lazy { PreferatorAdapter() }

    var config: PreferatorConfig = PreferatorConfig()
        set(value) {
            presenter.config = value
        }

    private val presenter: PreferatorPresenter by lazy { PreferatorPresenter(this, PreferatorDataSource(context)) }

    init {
        initView()
        orientation = VERTICAL
        presenter.start()
    }

    override fun render(preferences: Preferences) {
        adapter.items = preferences.items
        adapter.notifyDataSetChanged()
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.preferator_view, this)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
        recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }
}