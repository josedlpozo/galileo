package com.josedlpozo.galileo.preferator.view.editor

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import com.josedlpozo.galileo.preferator.model.Type

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
        val stringEditor = StringPrefEditor(context, listener = { preferences.edit().putString(prefKey, it).apply() })
        stringEditor.value = prefValue
        return stringEditor
    }

    private fun createIntEditorView(context: Context, preferences: SharedPreferences, prefKey: String, prefValue: Int): IntPrefEditor {
        val intEditor = IntPrefEditor(context, listener = { preferences.edit().putInt(prefKey, it).apply() })
        intEditor.value = prefValue
        return intEditor
    }

    private fun createLongEditorView(context: Context, preferences: SharedPreferences, prefKey: String, prefValue: Long): LongPrefEditor {
        val longEditor = LongPrefEditor(context, listener = { preferences.edit().putLong(prefKey, it).apply() })
        longEditor.value = prefValue
        return longEditor
    }

    private fun createFloatEditorView(context: Context, preferences: SharedPreferences, prefKey: String, prefValue: Float): FloatPrefEditor {
        val floatEditor = FloatPrefEditor(context, listener = { preferences.edit().putFloat(prefKey, it).apply() })
        floatEditor.value = prefValue
        return floatEditor
    }

    private fun createBooleanEditorView(context: Context, preferences: SharedPreferences, prefKey: String, prefValue: Boolean): BooleanPrefEditor {
        val booleanEditor = BooleanPrefEditor(context, listener = { preferences.edit().putBoolean(prefKey, it).apply() })
        booleanEditor.value = prefValue
        return booleanEditor
    }

    private fun createSetEditorView(context: Context, preferences: SharedPreferences, prefKey: String, prefValue: Set<String>): SetPrefEditor {
        val booleanEditor = SetPrefEditor(context, listener = { preferences.edit().putStringSet(prefKey, it).apply() })
        booleanEditor.value = prefValue
        return booleanEditor
    }

}