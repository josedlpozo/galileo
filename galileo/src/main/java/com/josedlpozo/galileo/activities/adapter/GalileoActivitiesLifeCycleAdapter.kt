/*
 * Copyright (C) 2018 josedlpozo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.josedlpozo.galileo.activities.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.activities.model.*

internal class GalileoActivitiesLifeCycleAdapter(private val events: List<Event>) : RecyclerView.Adapter<ActivityLifeCycleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityLifeCycleViewHolder =
            CreatedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false))

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: ActivityLifeCycleViewHolder, position: Int) {
        holder.bind(events[position])
    }
}

internal abstract class ActivityLifeCycleViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val textTitle: TextView by lazy { view.findViewById<TextView>(R.id.textTitle) }
    private val textSubtitle: TextView by lazy { view.findViewById<TextView>(R.id.textSubtitle) }
    private val textCaption: TextView by lazy { view.findViewById<TextView>(R.id.textCaption) }

    abstract val Event.title: String
    abstract val Event.subtitle: String
    abstract val Event.caption: String

    fun bind(event: Event) = with(event) {
        textTitle.text = title
        textSubtitle.text = subtitle
        if (caption.isEmpty()) textCaption.visibility = View.GONE else textCaption.text = caption
    }
}

internal class CreatedViewHolder(view: View) : ActivityLifeCycleViewHolder(view) {

    override val Event.title: String
        get() = name

    override val Event.subtitle: String
        get() = activityName

    override val Event.caption: String
        get() = when(this) {
            is Created -> "${extras.size} items"
            is SavedInstanceState -> "${bundleValues.size} items"
            else -> ""
        }

}

