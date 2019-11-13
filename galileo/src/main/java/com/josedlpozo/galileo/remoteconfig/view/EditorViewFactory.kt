package com.josedlpozo.galileo.remoteconfig.view

import android.content.Context
import android.view.View
import com.josedlpozo.galileo.remoteconfig.RemoteConfigKey
import com.josedlpozo.galileo.remoteconfig.RemoteConfigKeyBoolean
import com.josedlpozo.galileo.remoteconfig.RemoteConfigKeyDouble
import com.josedlpozo.galileo.remoteconfig.RemoteConfigKeyLong
import com.josedlpozo.galileo.remoteconfig.RemoteConfigKeyString
import java.lang.IllegalArgumentException

internal class EditorViewFactory {

    fun createView(context: Context, remoteConfigKey: RemoteConfigKey, listener: (RemoteConfigKey) -> Unit): View = when (remoteConfigKey) {
        is RemoteConfigKeyString -> createStringEditorView(context, remoteConfigKey, listener)
        is RemoteConfigKeyLong -> createLongEditorView(context, remoteConfigKey, listener)
        is RemoteConfigKeyDouble -> createDoubleEditorView(context, remoteConfigKey, listener)
        is RemoteConfigKeyBoolean -> createBooleanEditorView(context, remoteConfigKey, listener)
        else -> throw IllegalArgumentException()
    }

    private fun createStringEditorView(context: Context, remoteConfigKey: RemoteConfigKeyString, listener: (RemoteConfigKey) -> Unit): StringPrefEditor {
        val stringEditor = StringPrefEditor(context, key = remoteConfigKey, listener = listener)
        stringEditor.value = remoteConfigKey.value
        return stringEditor
    }

    private fun createLongEditorView(context: Context, remoteConfigKey: RemoteConfigKeyLong, listener: (RemoteConfigKey) -> Unit): LongPrefEditor {
        val longEditor = LongPrefEditor(context, key = remoteConfigKey, listener = listener)
        longEditor.value = remoteConfigKey.value
        return longEditor
    }

    private fun createDoubleEditorView(context: Context, remoteConfigKey: RemoteConfigKeyDouble, listener: (RemoteConfigKey) -> Unit): DoublePrefEditor {
        val floatEditor = DoublePrefEditor(context, key = remoteConfigKey, listener = listener)
        floatEditor.value = remoteConfigKey.value
        return floatEditor
    }

    private fun createBooleanEditorView(context: Context, remoteConfigKey: RemoteConfigKeyBoolean, listener: (RemoteConfigKey) -> Unit): BooleanPrefEditor {
        val booleanEditor = BooleanPrefEditor(context, key = remoteConfigKey, listener = listener)
        booleanEditor.value = remoteConfigKey.value
        return booleanEditor
    }


}