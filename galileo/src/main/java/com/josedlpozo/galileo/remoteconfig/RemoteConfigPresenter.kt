package com.josedlpozo.galileo.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue

internal class RemoteConfigPresenter(private val view: View) {

    internal fun start(): List<RemoteConfigKey> =
        FirebaseRemoteConfig.getInstance().all.map {
            it.value.toRemoteConfigKey(it.key)
        }.filter { it !is RemoteConfigKeyIgnore }.also {
            view.render(it)
        }

    private fun FirebaseRemoteConfigValue.toRemoteConfigKey(key: String): RemoteConfigKey {
        val asBoolean = try {
            asBoolean()
        } catch (exception: Exception) {
            null
        }

        if (asBoolean != null) return RemoteConfigKeyBoolean(key, asBoolean)

        val asDouble = try {
            asDouble()
        } catch (exception: Exception) {
            null
        }

        if (asDouble != null) return RemoteConfigKeyDouble(key, asDouble)

        val asLong = try {
            asLong()
        } catch (exception: Exception) {
            null
        }

        if (asLong != null) return RemoteConfigKeyLong(key, asLong)

        val asString = try {
            asString()
        } catch (exception: Exception) {
            null
        }

        if (asString != null) return RemoteConfigKeyString(key, asString)

        return RemoteConfigKeyIgnore(key)
    }

    internal interface View {
        fun render(values: List<RemoteConfigKey>)
    }
}