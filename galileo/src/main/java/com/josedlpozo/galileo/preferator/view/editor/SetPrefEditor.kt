package com.josedlpozo.galileo.preferator.view.editor


import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import java.util.*
import java.util.Arrays.asList
import com.josedlpozo.galileo.R

internal class SetPrefEditor @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, listener: (Set<String>) -> Unit = {}) : FrameLayout(context, attrs, defStyleAttr) {

    private val valueView: EditText

    var value: Set<String>
        get() {
            val rawValue = valueView.text.toString()
            return stringToSet(rawValue)
        }
        set(value) = valueView.setText(setToString(value))

    init {
        LayoutInflater.from(context).inflate(R.layout.item_editor_string, this, true)
        valueView = findViewById(R.id.pref_value)
        valueView.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                listener(stringToSet(charSequence.toString()))
            }

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    private fun stringToSet(rawValue: String): Set<String> {
        val items = rawValue.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return HashSet(asList(*items))
    }

    private fun setToString(set: Set<String>): String {
        return TextUtils.join(",", set)
    }

}
