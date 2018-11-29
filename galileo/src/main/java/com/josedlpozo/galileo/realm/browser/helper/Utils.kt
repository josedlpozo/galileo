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
package com.josedlpozo.galileo.realm.browser.helper

import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import io.realm.DynamicRealmObject
import io.realm.RealmObject
import io.realm.RealmObjectSchema
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.util.Date

object Utils {

    fun createParametrizedName(field: Field): String {
        val pType = field.genericType as ParameterizedType
        var rawType = pType.rawType.toString()
        val rawTypeIndex = rawType.lastIndexOf(".")
        if (rawTypeIndex > 0) {
            rawType = rawType.substring(rawTypeIndex + 1)
        }

        var argument = pType.actualTypeArguments[0].toString()
        val argumentIndex = argument.lastIndexOf(".")
        if (argumentIndex > 0) {
            argument = argument.substring(argumentIndex + 1)
        }

        return "$rawType<$argument>"
    }

    fun createBlobValueString(blobValue: ByteArray?, limit: Int): String? {
        if (blobValue == null) {
            return null
        }

        val builder = StringBuilder("byte[] = ")
        builder.append("{")
        for (i in 0 until if (limit == 0) blobValue.size else Math.min(blobValue.size, limit)) {
            builder.append(blobValue[i].toString())
            if (i < blobValue.size - 1) {
                builder.append(", ")
            }
        }
        if (limit != 0) builder.append("...")
        builder.append("}")
        return builder.toString()
    }

    fun getFieldValueString(realmObject: DynamicRealmObject, field: Field): CharSequence {
        var valueString: String? = null

        if (isParametrizedField(field)) {
            valueString = createParametrizedName(field)
        } else if (isBlob(field)) {
            valueString = createBlobValueString(realmObject.getBlob(field.name), 8)
        } else {
            // Strings, Numbers, Objects
            val fieldValue = realmObject.get<Any>(field.name)
            if (fieldValue != null) {
                valueString = fieldValue.toString()
            }
        }

        return if (valueString == null) {
            // Display null in italics to be able to distinguish between null and a string that actually says "null"
            valueString = "null"
            val nullString = SpannableString(valueString)
            nullString.setSpan(StyleSpan(Typeface.ITALIC), 0, valueString.length, 0)
            nullString
        } else {
            valueString
        }
    }

    fun getPrimaryKeyFieldName(schema: RealmObjectSchema): String? {
        for (s in schema.fieldNames) {
            if (schema.isPrimaryKey(s)) {
                return s
            }
        }
        return null
    }


    fun isNumber(field: Field): Boolean = isLong(field) || isInteger(field) || isShort(field) || isByte(field) || isDouble(field) || isFloat(field)

    fun isLong(field: Field): Boolean = field.type.name == Long::class.java.name || field.type.name == "long"

    fun isInteger(field: Field): Boolean = field.type.name == Int::class.java.name || field.type.name == "int"

    fun isShort(field: Field): Boolean = field.type.name == Short::class.java.name || field.type.name == "short"

    fun isByte(field: Field): Boolean = field.type.name == Byte::class.java.name || field.type.name == "byte"

    fun isDouble(field: Field): Boolean = field.type.name == Double::class.java.name || field.type.name == "double"

    fun isFloat(field: Field): Boolean = field.type.name == Float::class.java.name || field.type.name == "float"

    fun isBoolean(field: Field): Boolean = field.type.name == Boolean::class.java.name || field.type.name == "boolean"

    fun isString(field: Field): Boolean = field.type.name == String::class.java.name

    fun isParametrizedField(field: Field): Boolean = field.genericType is ParameterizedType

    fun isBlob(field: Field): Boolean = field.type.name == ByteArray::class.java.name

    fun isDate(field: Field): Boolean = field.type.name == Date::class.java.name

    fun isRealmObjectField(field: Field): Boolean = RealmObject::class.java.isAssignableFrom(field.type)

    fun equals(a: Any?, b: Any): Boolean = a === b || a != null && a == b
}