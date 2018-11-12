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

internal class GalileoActivitiesEventAdapter(private val activityEvents: List<ActivityEvent>,
                                             private val onClick: (ActivityEvent) -> Unit) : RecyclerView.Adapter<ActivityEventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityEventViewHolder =
            ActivityEventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false), onClick)

    override fun getItemCount(): Int = activityEvents.size

    override fun onBindViewHolder(holder: ActivityEventViewHolder, position: Int) {
        holder.bind(activityEvents[position])
    }
}

internal class ActivityEventViewHolder(view: View, private val onClick: (ActivityEvent) -> Unit) : RecyclerView.ViewHolder(view) {

    private val textTitle: TextView by lazy { view.findViewById<TextView>(R.id.textTitle) }
    private val textSubtitle: TextView by lazy { view.findViewById<TextView>(R.id.textSubtitle) }
    private val textCaption: TextView by lazy { view.findViewById<TextView>(R.id.textCaption) }

    fun bind(activityEvent: ActivityEvent) = with(activityEvent) {
        textTitle.text = name
        textSubtitle.text = activityName
        textCaption.visibility = View.VISIBLE
        when(activityEvent) {
            is Created -> if (activityEvent.extras.isEmpty()) textCaption.visibility = View.GONE else textCaption.text = "${activityEvent.extras.size} items"
            is SavedInstanceState -> if (activityEvent.extras.isEmpty()) textCaption.visibility = View.GONE else textCaption.text = "${activityEvent.extras.size} items"
            else -> textCaption.visibility = View.GONE
        }
        itemView.setOnClickListener {
            onClick(activityEvent)
        }
    }
}

