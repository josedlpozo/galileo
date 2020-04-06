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
import android.view.ViewStub
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.josedlpozo.galileo.realm.R
import com.josedlpozo.galileo.realm.realmbrowser.helper.Utils
import io.realm.DynamicRealmObject
import io.realm.RealmObjectSchema
import java.lang.reflect.Field

internal class RealmBrowserViewBool(
    context: Context,
    realmObjectSchema: RealmObjectSchema,
    field: Field
) : RealmBrowserViewField(context, realmObjectSchema, field) {

    private lateinit var spinner: Spinner

    public override fun inflateViewStub() {
        val stub = findViewById<ViewStub>(R.id.realm_browser_stub)
        stub.layoutResource = R.layout.realm_browser_fieldview_spinner
        stub.inflate()
    }

    override fun initViewStubView() {
        spinner = findViewById(R.id.realm_browser_field_boolspinner)
        val adapter = ArrayAdapter.createFromResource(
            context,
            R.array.realm_browser_boolean,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    override val value: Any?
        get() {
            return if (realmObjectSchema.isNullable(realmField.name) && isFieldIsNullCheckBoxChecked) {
                null
            } else java.lang.Boolean.parseBoolean(spinner.selectedItem.toString())
        }

    override fun toggleAllowInput(allow: Boolean) {
        spinner.isEnabled = allow
    }

    override val isInputValid: Boolean = true

    override fun setRealmObject(realmObject: DynamicRealmObject) {
        if (Utils.isBoolean(realmField)) {
            spinner.setSelection(if (realmObject.getBoolean(realmField.name)) 0 else 1)
        } else {
            throw IllegalArgumentException()
        }
    }
}