package com.josedlpozo.galileo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.josedlpozo.galileo.lynx.GalileoLynx
import com.josedlpozo.galileo.ui.home.HomeFragment
import com.readystatesoftware.chuck.internal.ui.TransactionListView
import com.sloydev.preferator.Preferator
import com.sloydev.preferator.model.PreferatorConfig

internal class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        if (savedInstanceState == null) {
            val homeFragment = HomeFragment.newInstance()
            homeFragment.items = listOf(Preferator.view(this, PreferatorConfig()), GalileoLynx(this), TransactionListView(this))
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, homeFragment)
                    .commitNow()
        }
    }

}
