package com.josedlpozo.galileo.preferator.view.editor


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Switch
import com.josedlpozo.galileo.preferator.R

internal class BooleanPrefEditor @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, listener: (Boolean) -> Unit = {}) : FrameLayout(context, attrs, defStyleAttr) {

    private val valueView: Switch

    var value: Boolean
        get() = valueView.isChecked
        set(value) {
            valueView.isChecked = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.item_editor_boolean, this, true)
        valueView = findViewById(R.id.pref_value_boolean)
        valueView.setOnCheckedChangeListener { _, isChecked ->
            listener(isChecked)
        }
    }
}
