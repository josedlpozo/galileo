package com.sloydev.preferator.view.editor

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import com.sloydev.preferator.model.Type
import com.sloydev.preferator.view.editor.*

internal class EditorViewFactory {

    fun createView(context: Context, preferences: SharedPreferences, prefKey: String, prefValue: Any, prefType: Type): View = when (prefType) {
        Type.STRING -> createStringEditorView(context, preferences, prefKey, prefValue as String)
        Type.INT -> createIntEditorView(context, preferences, prefKey, prefValue as Int)
        Type.LONG -> createLongEditorView(context, preferences, prefKey, prefValue as Long)
        Type.FLOAT -> createFloatEditorView(context, preferences, prefKey, prefValue as Float)
        Type.BOOLEAN -> createBooleanEditorView(context, preferences, prefKey, prefValue as Boolean)
        Type.SET -> createSetEditorView(context, preferences, prefKey, prefValue as Set<String>)
    }

    private fun createStringEditorView(context: Context, preferences: SharedPreferences, prefKey: String, prefValue: String): StringPrefEditor {
        val stringEditor = StringPrefEditor(context)
        stringEditor.value = prefValue
        stringEditor.setOnStringValueChangeListener { newValue -> preferences.edit().putString(prefKey, newValue).apply() }
        return stringEditor
    }

    private fun createIntEditorView(context: Context, preferences: SharedPreferences, prefKey: String, prefValue: Int?): IntPrefEditor {
        val intEditor = IntPrefEditor(context)
        intEditor.value = prefValue
        intEditor.setOnIntValueChangeListener { newValue -> preferences.edit().putInt(prefKey, newValue!!).apply() }
        return intEditor
    }

    private fun createLongEditorView(context: Context, preferences: SharedPreferences, prefKey: String, prefValue: Long?): LongPrefEditor {
        val longEditor = LongPrefEditor(context)
        longEditor.value = prefValue
        longEditor.setOnLongValueChangeListener { newValue -> preferences.edit().putLong(prefKey, newValue!!).apply() }
        return longEditor
    }

    private fun createFloatEditorView(context: Context, preferences: SharedPreferences, prefKey: String, prefValue: Float?): FloatPrefEditor {
        val floatEditor = FloatPrefEditor(context)
        floatEditor.value = prefValue
        floatEditor.setOnFloatValueChangeListener { newValue -> preferences.edit().putFloat(prefKey, newValue!!).apply() }
        return floatEditor
    }

    private fun createBooleanEditorView(context: Context, preferences: SharedPreferences, prefKey: String, prefValue: Boolean?): BooleanPrefEditor {
        val booleanEditor = BooleanPrefEditor(context)
        booleanEditor.value = prefValue
        booleanEditor.setOnBooleanValueChangeListener { newValue -> preferences.edit().putBoolean(prefKey, newValue!!).apply() }
        return booleanEditor
    }

    private fun createSetEditorView(context: Context, preferences: SharedPreferences, prefKey: String, prefValue: Set<String>): SetPrefEditor {
        val booleanEditor = SetPrefEditor(context)
        booleanEditor.value = prefValue
        booleanEditor.setOnSetValueChangeListener { newValue -> preferences.edit().putStringSet(prefKey, newValue).apply() }
        return booleanEditor
    }

}