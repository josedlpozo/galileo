package com.josedlpozo.galileo.remoteconfig

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.ScrollView
import androidx.appcompat.widget.SearchView
import com.josedlpozo.galileo.remoteconfig.view.EditorViewFactory

internal class RemoteConfigView @JvmOverloads internal constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ScrollView(context, attr, defStyleAttr), RemoteConfigPresenter.View {

    private val presenter: RemoteConfigPresenter by lazy {
        RemoteConfigPresenter(
            this
        )
    }

    private val linearLayout = LinearLayout(context).apply {
        orientation = VERTICAL
    }
    private val searchView = SearchView(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == null) return false
                presenter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == null) return true
                if (newText.isEmpty()) presenter.start()
                return true
            }

        })
    }

    init {
        addView(linearLayout)
        linearLayout.addView(searchView)
        presenter.start()
    }

    override fun render(values: List<RemoteConfigKey>) {
        linearLayout.removeAllViews()
        linearLayout.addView(searchView)
        values.map {
            val view = EditorViewFactory().createView(context, it)
            linearLayout.addView(view)
        }
    }

    fun snapshot(): String = ""
}