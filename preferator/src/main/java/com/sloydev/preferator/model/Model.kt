package com.sloydev.preferator.model

import android.content.SharedPreferences
import java.io.Serializable

typealias PreferenceItem = Pair<String, Any>

data class Preference(val name: String, val items: List<PreferenceItem> = listOf(), val sharedPreferences: SharedPreferences)

data class Preferences(val items: List<Preference> = listOf())

data class PreferatorConfig(val showingSdkPreferences: Boolean = false) : Serializable