package com.josedlpozo.galileo.ui.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.*
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
    private lateinit var container: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        bottomBar = view.findViewById(R.id.navigation)
        this.container = view.findViewById(R.id.container)
        setHasOptionsMenu(true)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        viewModel.start(items)

        viewModel.items.observe(this, Observer { items ->
            if (items == null || items.isEmpty()) return@Observer

            bottomBar.menu.clear()
            items.map {
                bottomBar.menu.add(it.name).setIcon(it.icon)
            }

            container.removeAllViews()
            container.addView(items.first().view)

            bottomBar.setOnNavigationItemSelectedListener { item ->
                container.removeAllViews()
                items.find { it.name == item.title }?.let {
                    container.addView(it.view)
                }
                true
            }
        })

        viewModel.shareText.observe(this, Observer {
            if (it == null) return@Observer

            share(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.share, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when(item?.itemId) {
        R.id.share -> {
            viewModel.share()
            true
        }
        else -> false
    }

    private fun share(plainTraces: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, plainTraces)
        context?.startActivity(Intent.createChooser(sharingIntent, "Galileo"))
    }
}
