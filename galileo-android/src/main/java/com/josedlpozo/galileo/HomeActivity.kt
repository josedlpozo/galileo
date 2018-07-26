package com.josedlpozo.galileo

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.github.pedrovgs.lynx.LynxConfig
import com.github.pedrovgs.lynx.LynxView
import com.github.pedrovgs.lynx.model.Trace
import com.josedlpozo.galileo.items.GalileoItem
import com.josedlpozo.galileo.ui.home.HomeFragment
import com.sloydev.preferator.Preferator
import com.sloydev.preferator.model.PreferatorConfig
import java.util.Arrays.asList

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        if (savedInstanceState == null) {
            prefillPreferences()
            val homeFragment = HomeFragment.newInstance()
            homeFragment.items = listOf(Preferator.view(this, PreferatorConfig()), GalileoLynx(this))
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, homeFragment)
                    .commitNow()
        }
    }

    private fun prefillPreferences() {
        fill(getPreferences(Context.MODE_PRIVATE))
        fill(PreferenceManager.getDefaultSharedPreferences(this))
        fill(getSharedPreferences("another file", Context.MODE_PRIVATE))
        fill(getSharedPreferences("com.google.pruebas", Context.MODE_PRIVATE))
    }

    private fun fill(preferences: SharedPreferences) {
        preferences.edit()
                .putString("some_string", "a string value")
                .putInt("some_int", 42)
                .putLong("some_long", System.currentTimeMillis())
                .putBoolean("some_boolean", true)
                .putFloat("some_float", 3.14f)
                .putStringSet("some_set", HashSet<String>(asList<String>("a", "b", "c")))
                .apply()
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
    }

}

class GalileoLynx(context: Context): LynxView(context), GalileoItem {

    private val oldTraces : MutableList<Trace> = mutableListOf()

    init {
        lynxConfig = LynxConfig().apply {
            filter = getApplicationName()
        }
    }

    override fun showTraces(traces: MutableList<Trace>?, removedTraces: Int) {
        super.showTraces(traces, removedTraces)
        traces?.let {
            oldTraces.clear()
            oldTraces.addAll(it)
        }
    }

    private fun getApplicationName(): String {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(stringId)
    }

    override val name : String
        get() = "Lynx"

    override val icon : Int
        get() = android.R.drawable.stat_sys_warning

    override val view : View
        get() = this

    override fun snapshot() : String = oldTraces.joinToString("\n")

}
