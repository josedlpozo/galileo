package com.josedlpozo.galileo.preferator

import android.content.Context
import com.josedlpozo.galileo.preferator.model.PreferatorConfig
import com.josedlpozo.galileo.preferator.view.PreferatorView

internal object Preferator {

    fun view(context: Context, config: PreferatorConfig = PreferatorConfig()) = PreferatorView(context).apply { this.config = config }

}