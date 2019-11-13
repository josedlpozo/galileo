package com.josedlpozo.galileo.remoteconfig.view


import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.remoteconfig.RemoteConfigKey
import com.josedlpozo.galileo.remoteconfig.RemoteConfigKeyString

internal class StringPrefEditor @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    key: RemoteConfigKeyString
) : FrameLayout(context, attrs, defStyleAttr) {

    private val keyView: TextView
    private val valueView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.remote_config_value, this, true)

        keyView = findViewById(R.id.tvKey)
        valueView = findViewById(R.id.tvValue)

        keyView.text = key.key
        valueView.text = key.value
    }
}
