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

    fun createView(context: Context, remoteConfigKey: RemoteConfigKey): View = when (remoteConfigKey) {
        is RemoteConfigKeyString -> createStringEditorView(context, remoteConfigKey)
        is RemoteConfigKeyLong -> createLongEditorView(context, remoteConfigKey)
        is RemoteConfigKeyDouble -> createDoubleEditorView(context, remoteConfigKey)
        is RemoteConfigKeyBoolean -> createBooleanEditorView(context, remoteConfigKey)
        else -> throw IllegalArgumentException()
    }

    private fun createStringEditorView(context: Context, remoteConfigKey: RemoteConfigKeyString): StringPrefEditor =
        StringPrefEditor(context, key = remoteConfigKey)

    private fun createLongEditorView(context: Context, remoteConfigKey: RemoteConfigKeyLong): LongPrefEditor =
        LongPrefEditor(context, key = remoteConfigKey)

    private fun createDoubleEditorView(context: Context, remoteConfigKey: RemoteConfigKeyDouble): DoublePrefEditor =
        DoublePrefEditor(context, key = remoteConfigKey)

    private fun createBooleanEditorView(context: Context, remoteConfigKey: RemoteConfigKeyBoolean): BooleanPrefEditor =
        BooleanPrefEditor(context, key = remoteConfigKey)


}