package com.josedlpozo.galileo.preferator

import com.josedlpozo.galileo.core.GalileoPlugin
import com.josedlpozo.galileo.preferator.view.PreferatorGalileoItem

val preferatorPlugin: GalileoPlugin = { PreferatorGalileoItem(it) }