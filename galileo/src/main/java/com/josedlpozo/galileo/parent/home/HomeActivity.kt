package com.josedlpozo.galileo.parent.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.lynx.GalileoLynx
import com.josedlpozo.galileo.chuck.internal.ui.TransactionListView
import com.josedlpozo.galileo.preferator.Preferator
import com.josedlpozo.galileo.preferator.model.PreferatorConfig

internal class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.galileo_home_activity)
        if (savedInstanceState == null) {
            val homeFragment = HomeFragment.newInstance()
            homeFragment.items = listOf(Preferator.view(this, PreferatorConfig()), TransactionListView(this))
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, homeFragment)
                    .commitNow()
        }
    }

}