package com.josedlpozo.galileo.realm

import com.josedlpozo.galileo.core.GalileoPlugin

val realmPlugin: GalileoPlugin = { RealmGalileoItem(it) }