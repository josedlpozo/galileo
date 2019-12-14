package com.josedlpozo.galileo.chuck

import com.josedlpozo.galileo.chuck.ui.TransactionGalileoItem
import com.josedlpozo.galileo.core.GalileoPlugin

val chuckPlugin: GalileoPlugin = { TransactionGalileoItem(it) }