package com.josedlpozo.galileo.remoteconfig.view


import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.remoteconfig.RemoteConfigKey
import com.josedlpozo.galileo.remoteconfig.RemoteConfigKeyDouble

internal class DoublePrefEditor @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, key: RemoteConfigKey,
    listener: (RemoteConfigKey) -> Unit = {}
) : FrameLayout(context, attrs, defStyleAttr) {

    private val valueView: EditText

    var value: Double
        get() = java.lang.Double.parseDouble(valueView.text.toString())
        set(value) = valueView.setText(value.toString())

    init {
        LayoutInflater.from(context).inflate(R.layout.item_editor_float, this, true)
        valueView = findViewById(R.id.pref_value)
        valueView.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
                try {
                    val number: Double = if (charSequence != null) {
                        java.lang.Double.parseDouble(charSequence.toString())
                    } else {
                        0.0
                    }
                    listener(RemoteConfigKeyDouble(key.key, number))
                    valueView.error = null
                } catch (e: NumberFormatException) {
                    valueView.error = "Wrong integer format"
                }
            }

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {}
        })
    }
}
