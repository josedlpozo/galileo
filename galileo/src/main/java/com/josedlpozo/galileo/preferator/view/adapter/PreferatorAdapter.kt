package com.josedlpozo.galileo.preferator.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.preferator.model.Preference
import com.josedlpozo.galileo.preferator.view.PreferatorViewHolder
import com.josedlpozo.galileo.preferator.view.editor.EditorViewFactory

internal class PreferatorAdapter : RecyclerView.Adapter<PreferatorViewHolder>() {

    private val factory: EditorViewFactory by lazy { EditorViewFactory() }

    var items: List<Preference> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreferatorViewHolder =
        LayoutInflater.from(parent.context).inflate(R.layout.item_section, null, false).let { PreferatorViewHolder(it, factory) }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PreferatorViewHolder, position: Int) = holder.bind(items[position])
}