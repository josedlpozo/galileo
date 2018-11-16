package com.josedlpozo.galileo.realm

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.items.GalileoItem
import com.josedlpozo.galileo.realm.realmbrowser.files.FilesContract
import com.josedlpozo.galileo.realm.realmbrowser.files.FilesContract.Presenter
import com.josedlpozo.galileo.realm.realmbrowser.files.FilesPresenter
import com.josedlpozo.galileo.realm.realmbrowser.files.model.FilesPojo
import com.josedlpozo.galileo.realm.realmbrowser.files.view.FilesAdapter
import java.util.ArrayList

class RealmView @JvmOverloads internal constructor(context: Context, val attr: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attr, defStyleAttr), GalileoItem, FilesContract.View {

    override val name: String = "RealmBrowser"

    override val galileoView: View = this

    override val icon: Int = R.drawable.realm_browser_ic_rb

    override fun snapshot(): String = "RealmBrowser snapshot"

    private val presenter: FilesContract.Presenter = FilesPresenter()
    private lateinit var adapter: FilesAdapter

    init {
        initView()
        orientation = VERTICAL
    }

    override fun updateWithFiles(filesList: ArrayList<FilesPojo>) {
        adapter.swapList(filesList)
        if (filesList.size == 0) {
            // TODO show empty list placeholder
        }
    }

    override fun attachPresenter(presenter: Presenter?) {}

    override fun getViewContext(): Context = context

    override fun showToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.realm_browser_ac_recycler, this)
        val swipeRefreshLayout = findViewById<View>(R.id.swiperefresh) as SwipeRefreshLayout
        swipeRefreshLayout.isEnabled = false
        adapter = FilesAdapter(ArrayList(),
            FilesAdapter.OnFileSelectedListener { presenter.onFileSelected(it) })
        val recyclerView = findViewById<View>(R.id.realm_browser_recycler) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        presenter.attachView(this)
        presenter.requestForContentUpdate(context)
    }

}