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
package com.josedlpozo.galileo.more

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.parent.extensions.tint

internal class MoreEventAdapter(private val flowEvents: List<MoreItems>,
                                private val onClick: (MoreItems) -> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<MoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreViewHolder =
        MoreViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_more, parent, false), onClick)

    override fun getItemCount(): Int = flowEvents.size

    override fun onBindViewHolder(holder: MoreViewHolder, position: Int) {
        holder.bind(flowEvents[position])
    }
}

internal class MoreViewHolder(view: View, private val onClick: (MoreItems) -> Unit) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    private val textTitle: TextView by lazy { view.findViewById<TextView>(R.id.textTitle) }
    private val imageIcon: ImageView by lazy { view.findViewById<ImageView>(R.id.imageIcon) }

    fun bind(item: MoreItems) = with(item) {
        textTitle.text = item.item.name

        imageIcon.setBackgroundDrawable(itemView.resources.getDrawable(item.item.icon).tint(itemView.resources.getColor(R.color.galileocolor_accent)))

        itemView.setOnClickListener {
            onClick(item)
        }
    }
}