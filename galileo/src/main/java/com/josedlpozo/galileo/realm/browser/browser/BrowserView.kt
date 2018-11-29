package com.josedlpozo.galileo.realm.browser.browser

import android.content.Context
import io.realm.DynamicRealmObject
import java.lang.reflect.Field
import java.util.AbstractList

interface BrowserView {

    fun getViewContext(): Context

    fun showMenu()

    fun updateWithRealmObjects(objects: AbstractList<out DynamicRealmObject>)

    fun updateWithFABVisibility(visible: Boolean)

    fun updateWithTitle(title: String)

    fun updateWithTextWrap(wrapText: Boolean)

    fun updateWithFieldList(fields: List<Field>, selectedFieldIndices: Array<Int>)

    fun showInformation(numberOfRows: Long)

}