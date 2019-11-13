package com.josedlpozo.galileo.remoteconfig

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.ScrollView
import com.josedlpozo.galileo.remoteconfig.view.EditorViewFactory

internal class RemoteConfigView @JvmOverloads internal constructor(context: Context, val attr: AttributeSet? = null, defStyleAttr: Int = 0)
    : ScrollView(context, attr, defStyleAttr), RemoteConfigPresenter.View {

    private val presenter: RemoteConfigPresenter by lazy { RemoteConfigPresenter(this) }

    private val linearLayout = LinearLayout(context).apply {
        orientation = VERTICAL
    }

    init {
        addView(linearLayout)
        presenter.start()
    }

    override fun render(values: List<RemoteConfigKey>) {
        linearLayout.removeAllViews()
        values.map {
            val view = EditorViewFactory().createView(context, it) {

            }

            linearLayout.addView(view)
        }
    }

    fun snapshot(): String = ""
}