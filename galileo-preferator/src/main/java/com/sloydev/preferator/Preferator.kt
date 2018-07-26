package com.sloydev.preferator

import android.content.Context
import com.sloydev.preferator.model.PreferatorConfig
import com.sloydev.preferator.view.PreferatorView

object Preferator {

    fun view(context: Context, config: PreferatorConfig = PreferatorConfig()) = PreferatorView(context).apply { this.config = config }
}