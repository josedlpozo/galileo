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
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.ViewStub
import android.widget.EditText
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import com.google.android.material.snackbar.Snackbar
import com.josedlpozo.galileo.realm.R
import com.josedlpozo.galileo.realm.realmbrowser.helper.Utils
import io.realm.DynamicRealmObject
import io.realm.RealmObjectSchema
import java.lang.reflect.Field

internal class RealmBrowserViewNumber(
    context: Context, realmObjectSchema: RealmObjectSchema,
    field: Field
) : RealmBrowserViewField(context, realmObjectSchema, field) {
    private var isValidData = false
    private lateinit var fieldEditText: EditText

    public override fun inflateViewStub() {
        val stub = findViewById<ViewStub>(R.id.realm_browser_stub)
        stub.layoutResource = R.layout.realm_browser_fieldview_edittext
        stub.inflate()
    }

    override fun initViewStubView() {
        fieldEditText = findViewById(R.id.realm_browser_field_edittext)
        fieldEditText.maxLines = 1
        fieldEditText.addTextChangedListener(createTextWatcher())
        if (Utils.isDouble(realmField) || Utils.isFloat(realmField)) {
            fieldEditText.inputType =
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
        } else {
            fieldEditText.inputType =
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        }
    }

    override val value: Any?
        get() {
            if (realmObjectSchema.isNullable(realmField.name) && isFieldIsNullCheckBoxChecked) {
                return null
            }

            var value = fieldEditText.text.toString()
            if (value.isEmpty()) {
                value = "0"
            }

            return when {
                Utils.isByte(realmField) -> java.lang.Byte.parseByte(value)
                Utils.isShort(realmField) -> java.lang.Short.parseShort(value)
                Utils.isInteger(realmField) -> Integer.parseInt(value)
                Utils.isLong(realmField) -> java.lang.Long.parseLong(value)
                Utils.isDouble(realmField) -> java.lang.Double.parseDouble(value)
                Utils.isFloat(realmField) -> java.lang.Float.parseFloat(value)
                else -> null
            }
        }

    override fun toggleAllowInput(allow: Boolean) {
        fieldEditText.isEnabled = allow
    }

    override val isInputValid: Boolean
        get() = isValidData

    override fun setRealmObject(realmObject: DynamicRealmObject) {
        if (Utils.isNumber(realmField)) {
            fieldEditText.setText(realmObject.get<Any>(realmField.name).toString())
        } else {
            throw IllegalArgumentException()
        }
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                try {
                    if (!s.toString().isEmpty()) {
                        when {
                            Utils.isByte(realmField) -> java.lang.Byte.parseByte(s.toString())
                            Utils.isShort(realmField) -> java.lang.Short.parseShort(s.toString())
                            Utils.isInteger(realmField) -> Integer.parseInt(s.toString())
                            Utils.isLong(realmField) -> java.lang.Long.parseLong(s.toString())
                            Utils.isDouble(realmField) -> java.lang.Double.parseDouble(s.toString())
                            Utils.isFloat(realmField) -> java.lang.Float.parseFloat(s.toString())
                        }
                    }
                    fieldInfoImageView.visibility = View.GONE
                    isValidData = true
                    setBackgroundColor(getColor(context, android.R.color.transparent))
                } catch (e: NumberFormatException) {
                    isValidData = false
                    fieldInfoImageView.visibility = View.VISIBLE
                    fieldInfoImageView.setImageDrawable(
                        getDrawable(
                            context,
                            R.drawable.realm_browser_ic_warning_black_24dp
                        )
                    )
                    fieldInfoImageView.setColorFilter(
                        getColor(
                            context,
                            R.color.realm_browser_error
                        ), SRC_ATOP
                    )
                    fieldInfoImageView.setOnClickListener { showError(s) }
                    setBackgroundColor(getColor(context, R.color.realm_browser_error_light))
                }
            }
        }
    }

    private fun showError(s: Editable) {
        Snackbar.make(
            this,
            s.toString() + " does not fit data type " + realmField.type.simpleName,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}