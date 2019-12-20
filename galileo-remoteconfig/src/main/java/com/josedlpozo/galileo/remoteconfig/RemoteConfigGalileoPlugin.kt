package com.josedlpozo.galileo.remoteconfig

import com.josedlpozo.galileo.core.GalileoPlugin

val remoteConfigPlugin: GalileoPlugin = { RemoteConfigGalileoItem(it) }