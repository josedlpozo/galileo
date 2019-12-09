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
package com.josedlpozo.galileo.realm.realmbrowser.browser.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.josedlpozo.galileo.realm.R
import com.josedlpozo.galileo.realm.realmbrowser.helper.Utils
import io.realm.DynamicRealmObject
import java.lang.reflect.Field

internal class RealmBrowserAdapter(private val listener: (DynamicRealmObject) -> Unit) :
    androidx.recyclerview.widget.RecyclerView.Adapter<RealmBrowserAdapter.ViewHolder>() {

    private var dynamicRealmObjects: List<DynamicRealmObject> = listOf()
    private var fieldList: List<Field> = listOf()

    fun setFieldList(fieldList: List<Field>) {
        this.fieldList = fieldList
    }

    fun setRealmList(realmObjects: List<DynamicRealmObject>) {
        this.dynamicRealmObjects = realmObjects
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.realm_browser_item_realm_browser,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = dynamicRealmObjects.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.realm_browser_grey
                )
            )
        } else {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.realm_browser_white
                )
            )
        }

        if (fieldList.isEmpty()) {
            holder.txtIndex.text = null
            holder.txtColumn1.text = null
            holder.txtColumn2.text = null
            holder.txtColumn3.text = null
        } else {
            holder.txtIndex.text = position.toString()

            val realmObject = dynamicRealmObjects[position]
            initRowWeight(holder)
            initRowText(holder, realmObject)
        }
    }

    private fun initRowWeight(holder: ViewHolder) {
        val layoutParams2 = createLayoutParams()
        val layoutParams3 = createLayoutParams()

        when {
            fieldList.size == 1 -> {
                layoutParams2.weight = 0f
                layoutParams3.weight = 0f
            }
            fieldList.size == 2 -> {
                layoutParams2.weight = 1f
                layoutParams3.weight = 0f
            }
            fieldList.size == 3 -> {
                layoutParams2.weight = 1f
                layoutParams3.weight = 1f
            }
        }
        holder.txtColumn2.layoutParams = layoutParams2
        holder.txtColumn3.layoutParams = layoutParams3
    }

    private fun initRowText(holder: ViewHolder, realmObject: DynamicRealmObject) {
        when {
            fieldList.size == 1 -> {
                initFieldText(holder.txtColumn1, realmObject, fieldList[0])
                holder.txtColumn2.text = null
                holder.txtColumn3.text = null
            }
            fieldList.size == 2 -> {
                initFieldText(holder.txtColumn1, realmObject, fieldList[0])
                initFieldText(holder.txtColumn2, realmObject, fieldList[1])
                holder.txtColumn3.text = null
            }
            fieldList.size == 3 -> {
                initFieldText(holder.txtColumn1, realmObject, fieldList[0])
                initFieldText(holder.txtColumn2, realmObject, fieldList[1])
                initFieldText(holder.txtColumn3, realmObject, fieldList[2])
            }
        }
    }

    private fun initFieldText(txtColumn: TextView, realmObject: DynamicRealmObject, field: Field) {
        txtColumn.text = Utils.getFieldValueString(realmObject, field)
        txtColumn.setOnClickListener { listener.invoke(realmObject) }
    }

    private fun createLayoutParams(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    internal class ViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {
        val txtIndex: TextView = v.findViewById(R.id.realm_browser_txtIndex)
        val txtColumn1: TextView = v.findViewById(R.id.realm_browser_txtColumn1)
        val txtColumn2: TextView = v.findViewById(R.id.realm_browser_txtColumn2)
        val txtColumn3: TextView = v.findViewById(R.id.realm_browser_txtColumn3)
    }
}