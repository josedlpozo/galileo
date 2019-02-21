package com.josedlpozo.galileo.more

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.config.ConfigRepository
import kotlinx.android.synthetic.main.galileo_plugin_activity.linearRoot

class PluginActivity : AppCompatActivity() {

    companion object {
        private const val PLUGIN_KEY = "plugin_key"

        fun start(context: Context, position: Long) = Intent(context, PluginActivity::class.java).apply {
            putExtra(PLUGIN_KEY, position)
        }.also(context::startActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.galileo_plugin_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val position = intent.getLongExtra(PLUGIN_KEY, 0)
        val plugin = ConfigRepository.more.find { it.id == position }
        if (plugin == null) finish()
        else {
            val item = plugin.plugin.invoke(this)
            title = item.name

            linearRoot.addView(item.view)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when {
        item.itemId == android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}