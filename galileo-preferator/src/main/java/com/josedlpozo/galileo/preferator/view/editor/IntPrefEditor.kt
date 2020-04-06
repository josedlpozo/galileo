package com.josedlpozo.galileo.preferator.view.editor


import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import com.josedlpozo.galileo.preferator.R

internal class IntPrefEditor @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    listener: (Int) -> Unit = {}
) : FrameLayout(context, attrs, defStyleAttr) {

    private val valueView: EditText

    var value: Int
        get() = Integer.parseInt(valueView.text.toString())
        set(value) = valueView.setText(value.toString())

    init {
        LayoutInflater.from(context).inflate(R.layout.item_editor_int, this, true)
        valueView = findViewById(R.id.pref_value)
        valueView.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
                try {
                    val number: Int = if (charSequence != null) {
                        Integer.parseInt(charSequence.toString())
                    } else {
                        0
                    }
                    listener(number)
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
