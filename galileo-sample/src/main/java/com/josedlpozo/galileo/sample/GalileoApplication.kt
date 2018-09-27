package com.josedlpozo.galileo.sample

import android.app.Application
import com.josedlpozo.galileo.Galileo

class GalileoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Galileo(this)
    }
}