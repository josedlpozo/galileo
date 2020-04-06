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
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import com.google.android.material.snackbar.Snackbar
import com.josedlpozo.galileo.realm.R
import io.realm.DynamicRealmObject
import io.realm.RealmFieldType
import io.realm.RealmObjectSchema
import java.lang.reflect.Field

internal abstract class RealmBrowserViewField(
    context: Context,
    protected val realmObjectSchema: RealmObjectSchema,
    val realmField: Field
) : LinearLayout(context) {

    private lateinit var tvFieldName: TextView
    private lateinit var tvFieldType: TextView
    private lateinit var ivFieldPrimaryKey: ImageView
    protected lateinit var fieldInfoImageView: ImageView
        private set
    private lateinit var cbxFieldIsNull: CheckBox

    open val fieldTypeString: String = realmField.type.simpleName

    abstract val value: Any?

    abstract val isInputValid: Boolean

    protected val isFieldIsNullCheckBoxChecked: Boolean
        get() = cbxFieldIsNull.isChecked

    init {

        initHeaderViews(context)
        inflateViewStub()
        initViewStubView()

        tvFieldName.text = realmField.name
        tvFieldType.text = fieldTypeString

        setupNullableCheckBox(realmField)
        setupPrimaryKeyImageView(realmField)
    }

    private fun initHeaderViews(context: Context) {
        orientation = LinearLayout.VERTICAL

        LayoutInflater.from(context).inflate(R.layout.realm_browser_fieldview, this)

        tvFieldName = findViewById(R.id.realm_browser_field_name)
        tvFieldType = findViewById(R.id.realm_browser_field_type)
        cbxFieldIsNull = findViewById(R.id.realm_browser_field_setnull)
        ivFieldPrimaryKey = findViewById(R.id.realm_browser_field_primarykey)
        fieldInfoImageView = findViewById(R.id.realm_browser_field_info)
    }

    private fun setupPrimaryKeyImageView(field: Field) {
        if (realmObjectSchema.isPrimaryKey(field.name)) {
            ivFieldPrimaryKey.setImageDrawable(
                getDrawable(
                    context,
                    R.drawable.realm_browser_ic_vpn_key_black_24dp
                )
            )
            ivFieldPrimaryKey.visibility = View.VISIBLE
        } else if (realmObjectSchema.isRequired(field.name)) {
            // TODO set key drawable with star
        } else {
            ivFieldPrimaryKey.visibility = View.GONE
        }
    }

    private fun setupNullableCheckBox(field: Field) {
        if (realmObjectSchema.isNullable(field.name) && realmObjectSchema.getFieldType(field.name) != RealmFieldType.LIST) {
            cbxFieldIsNull.setOnCheckedChangeListener { _, isChecked -> toggleAllowInput(!isChecked) }
        } else {
            cbxFieldIsNull.visibility = View.GONE
        }
    }

    fun togglePrimaryKeyError(show: Boolean) {
        ivFieldPrimaryKey.setImageDrawable(
            getDrawable(
                context,
                R.drawable.realm_browser_ic_vpn_key_black_24dp
            )
        )
        if (show) {
            ivFieldPrimaryKey.setColorFilter(
                getColor(context, R.color.realm_browser_error),
                SRC_ATOP
            )
            ivFieldPrimaryKey.setOnClickListener { v ->
                Snackbar.make(
                    this,
                    "Primary key \"$value\" already exists.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            setBackgroundColor(getColor(context, R.color.realm_browser_error_light))
        } else {
            ivFieldPrimaryKey.colorFilter = null
            ivFieldPrimaryKey.setOnClickListener(null)
            setBackgroundColor(getColor(context, android.R.color.transparent))
        }
    }

    protected fun updateFieldIsNullCheckBoxValue(checked: Boolean) {
        cbxFieldIsNull.isChecked = checked
    }

    protected abstract fun inflateViewStub()

    abstract fun initViewStubView()

    abstract fun toggleAllowInput(allow: Boolean)

    abstract fun setRealmObject(realmObject: DynamicRealmObject)
}