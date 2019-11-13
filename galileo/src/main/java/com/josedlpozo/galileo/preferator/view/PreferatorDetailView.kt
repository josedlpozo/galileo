package com.josedlpozo.galileo.preferator.view

import android.content.Context
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.preferator.SdkFilter
import com.josedlpozo.galileo.preferator.model.Preference
import com.josedlpozo.galileo.preferator.model.Type
import com.josedlpozo.galileo.preferator.view.editor.EditorViewFactory

internal class PreferatorDetailView @JvmOverloads internal constructor(context: Context,
            attr: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attr, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.item_section, this)
    }

    private val factory by lazy { EditorViewFactory() }

    private val sectionNameContainer by lazy { findViewById<RelativeLayout>(R.id.section_name_container) }
    private val sectionNameView by lazy { findViewById<TextView>(R.id.section_name) }
    private val sectionArrowView by lazy { findViewById<ImageView>(R.id.section_arrow) }
    private val itemsView by lazy { findViewById<ViewGroup>(R.id.section_items) }

    fun render(preference: Preference) {
        sectionNameView.text = preference.name
        sectionNameContainer.setOnClickListener {
            if (itemsView.visibility == View.VISIBLE) {
                itemsView.visibility = View.GONE
                sectionArrowView.setImageResource(R.drawable.ic_arrow_expand_black_24dp)
            } else {
                itemsView.visibility = View.VISIBLE
                sectionArrowView.setImageResource(R.drawable.ic_arrow_collapse_black_24dp)
            }
        }

        itemsView.removeAllViews()

        preference.items.map {
            val prefKey = it.first
            val prefValue = it.second
            val prefType = Type.of(prefValue)

            val itemView = LayoutInflater.from(context).inflate(R.layout.item_preference, itemsView, false)
            val nameView = itemView.findViewById(R.id.pref_name) as TextView
            val typeView = itemView.findViewById(R.id.pref_type) as TextView
            val moreView = itemView.findViewById(R.id.pref_more) as ImageView

            nameView.text = prefKey
            typeView.text = prefType.typeName

            val editorContainer = itemView.findViewById(R.id.pref_value_editor_container) as ViewGroup
            val editorView = factory.createView(context, preference.sharedPreferences, prefKey, prefValue, prefType)
            editorContainer.addView(editorView)


            val moreOptionsMenu = PopupMenu(context, moreView)
            moreOptionsMenu.inflate(R.menu.pref_more_options)
            moreOptionsMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                if (item.itemId == R.id.menu_pref_delete) {
                    preference.sharedPreferences.edit().remove(prefKey).apply()
                    itemsView.removeView(itemView)
                    return@OnMenuItemClickListener true
                }
                false
            })
            moreView.setOnClickListener { moreOptionsMenu.show() }


            itemsView.addView(itemView)
        }
    }
}