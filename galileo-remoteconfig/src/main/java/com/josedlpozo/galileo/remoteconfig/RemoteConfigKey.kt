package com.josedlpozo.galileo.remoteconfig

internal open class RemoteConfigKey(open val key: String)

internal open class RemoteConfigKeyBoolean(override val key: String, val value: Boolean): RemoteConfigKey(key)
internal open class RemoteConfigKeyDouble(override val key: String, val value: Double): RemoteConfigKey(key)
internal open class RemoteConfigKeyLong(override val key: String, val value: Long): RemoteConfigKey(key)
internal open class RemoteConfigKeyString(override val key: String, val value: String): RemoteConfigKey(key)

internal open class RemoteConfigKeyIgnore(override val key: String): RemoteConfigKey(key)