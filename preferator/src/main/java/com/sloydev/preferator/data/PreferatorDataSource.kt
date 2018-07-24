package com.sloydev.preferator.data

import android.content.Context
import android.content.SharedPreferences
import com.sloydev.preferator.SdkFilter
import com.sloydev.preferator.model.PreferatorConfig
import com.sloydev.preferator.model.Preference
import com.sloydev.preferator.model.PreferenceItem
import com.sloydev.preferator.model.Preferences
import java.io.File

internal class PreferatorDataSource(private val context: Context) {

    fun get(config: PreferatorConfig): Preferences = File(context.applicationInfo.dataDir + "/shared_prefs").list().map(::truncateXmlExtension)
        .filter { if (SdkFilter.isSdkPreference(it)) config.showingSdkPreferences else true }
        .sortedWith(compareBy({ SdkFilter.isSdkPreference(it) }, { it }))
        .map(::extractItems)
        .let(::Preferences)

    private fun truncateXmlExtension(it: String): String = if (it.endsWith(".xml"))
        it.substring(0, it.indexOf(".xml"))
    else
        it

    private fun extractItems(prefsName: String): Preference =
        (getSharedPreferences(prefsName).all.map { it.key to it.value }.filter { it.second != null } as List<PreferenceItem>).let {
            Preference(prefsName, it, getSharedPreferences(prefsName))
        }

    private fun getSharedPreferences(name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_MULTI_PROCESS)

}