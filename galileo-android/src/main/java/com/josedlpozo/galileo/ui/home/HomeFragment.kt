package com.josedlpozo.galileo.ui.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.items.GalileoItem

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    var items: List<GalileoItem> = listOf()

    private lateinit var viewModel: HomeViewModel
    private lateinit var bottomBar: BottomNavigationView
    private lateinit var prueba: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        bottomBar = view.findViewById(R.id.navigation)
        prueba = view.findViewById(R.id.container)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        viewModel.start(items)

        viewModel.prueba.observe(this, Observer { items ->
            if (items == null || items.isEmpty()) return@Observer

            bottomBar.menu.clear()
            items.map {
                bottomBar.menu.add(it.name).setIcon(it.icon)
            }

            prueba.removeAllViews()
            prueba.addView(items.first().view)

            bottomBar.setOnNavigationItemSelectedListener { item ->
                prueba.removeAllViews()
                items.find { it.name == item.title }?.let {
                    prueba.addView(it.view)
                }
                true
            }
        })
    }
}
