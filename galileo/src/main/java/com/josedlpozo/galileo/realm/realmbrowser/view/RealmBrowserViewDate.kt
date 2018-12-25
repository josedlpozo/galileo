/*
 * The MIT License (MIT)
 *
 * Original Work: Copyright (c) 2015 Danylyk Dmytro
 *
 * Modified Work: Copyright (c) 2015 Rottmann, Jonas
 *
 * Modified Work: Copyright (c) 2018 vicfran
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.josedlpozo.galileo.realm.realmbrowser.view

import android.content.Context
import android.graphics.PorterDuff.Mode.SRC_ATOP
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat.getColor
import android.support.v4.content.ContextCompat.getDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewStub
import android.widget.*
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.realm.realmbrowser.helper.Utils
import io.realm.DynamicRealmObject
import io.realm.RealmObjectSchema
import java.lang.reflect.Field
import java.util.*

internal class RealmBrowserViewDate(context: Context, realmObjectSchema: RealmObjectSchema, field: Field) : RealmBrowserViewField(context, realmObjectSchema, field) {

    private lateinit var textView: TextView
    private lateinit var editText: EditText
    private lateinit var infoImageView: ImageView
    private lateinit var buttonPicker: Button
    private lateinit var buttonNow: Button
    private lateinit var newDateValue: Date

    public override fun inflateViewStub() {
        val stub = findViewById<ViewStub>(R.id.realm_browser_stub)
        stub.layoutResource = R.layout.realm_browser_fieldview_date
        stub.inflate()
    }

    override fun initViewStubView() {
        textView = findViewById(R.id.realm_browser_field_date_textview)
        editText = findViewById(R.id.realm_browser_field_date_edittext)
        buttonPicker = findViewById(R.id.realm_browser_field_date_button_picker)
        buttonNow = findViewById(R.id.realm_browser_field_date_button_now)
        infoImageView = findViewById(R.id.realm_browser_field_date_infoimageview)

        editText.addTextChangedListener(createTextWatcher())

        buttonPicker.setOnClickListener { Toast.makeText(context, "TODO", Toast.LENGTH_SHORT).show() }
        buttonNow.setOnClickListener {
            newDateValue = Date(System.currentTimeMillis())
            editText.setText(newDateValue.time.toString())
            textView.text = newDateValue.toString()
        }
        infoImageView.setOnClickListener { Snackbar.make(this, "Time in milliseconds since epoch.", Snackbar.LENGTH_SHORT).show() }
    }

    override val value: Any?
        get () {
            return if (isFieldIsNullCheckBoxChecked) {
                null
            } else {
                newDateValue
            }
        }

    override fun toggleAllowInput(allow: Boolean) {
        textView.isEnabled = allow
        textView.visibility = if (allow) View.VISIBLE else View.GONE
        editText.visibility = if (allow) View.VISIBLE else View.GONE
        infoImageView.visibility = if (allow) View.VISIBLE else View.GONE
        buttonPicker.visibility = if (allow) View.VISIBLE else View.GONE
        buttonNow.visibility = if (allow) View.VISIBLE else View.GONE
    }

    override val isInputValid: Boolean
        get() {
            return validateInput(editText.text.toString())
        }

    override fun setRealmObject(realmObject: DynamicRealmObject) {
        if (Utils.isDate(realmField)) {
            if (realmObject.getDate(realmField.name) == null) {
                updateFieldIsNullCheckBoxValue(true)
            } else {
                editText.setText(realmObject.getDate(realmField.name).time.toString())
                textView.text = realmObject.getDate(realmField.name).toString()
            }
        } else {
            throw IllegalArgumentException()
        }
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                validateInput(s.toString())
            }
        }
    }

    private fun validateInput(s: String): Boolean {
        try {
            if (!s.isEmpty()) {
                newDateValue = Date(java.lang.Long.parseLong(s))
                textView.text = newDateValue.toString()
            }
            fieldInfoImageView.visibility = View.GONE
            setBackgroundColor(getColor(context, android.R.color.transparent))
            return true
        } catch (e: NumberFormatException) {
            fieldInfoImageView.visibility = View.VISIBLE
            fieldInfoImageView.setImageDrawable(getDrawable(context, R.drawable.realm_browser_ic_warning_black_24dp))
            fieldInfoImageView.setColorFilter(getColor(context, R.color.realm_browser_error), SRC_ATOP)
            fieldInfoImageView.setOnClickListener { showError(s) }
            setBackgroundColor(getColor(context, R.color.realm_browser_error_light))
            return false
        }

    }

    private fun showError(s: String) {
        Snackbar.make(this, s + " does not fit data type " + realmField.type.simpleName, Snackbar.LENGTH_SHORT).show()
    }
}