package com.josedlpozo.galileo.preferator.view.editor


import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import com.josedlpozo.galileo.R

internal class StringPrefEditor @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, listener: (String) -> Unit = {}) : FrameLayout(context, attrs, defStyleAttr) {

    private val valueView: EditText

    var value: String
        get() = valueView.text.toString()
        set(value) = valueView.setText(value)

    init {
        LayoutInflater.from(context).inflate(R.layout.item_editor_string, this, true)
        valueView = findViewById(R.id.pref_value)
        valueView.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                listener(charSequence.toString())
            }

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {}
        })
    }
}
