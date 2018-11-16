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
package com.josedlpozo.galileo.flow

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import arrow.core.fix
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.flow.adapter.FlowEventAdapter
import com.josedlpozo.galileo.items.GalileoItem

class FlowView @JvmOverloads constructor(context: Context,
                                         attr: AttributeSet? = null,
                                         defStyleAttr: Int = 0) : RecyclerView(context, attr, defStyleAttr), GalileoItem {

    init {
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL))

        FlowEventTry.useCase.get().fix().map {
            adapter = FlowEventAdapter(it) {
                ActivityEventDetailActivity.start(context, it.id)
            }
            if (it.isNotEmpty()) scrollToPosition(it.size - 1)
        }
    }

    override val view: View = this

    override val name: String = "Flow"

    override val icon: Int = R.drawable.ic_activity

    override fun snapshot(): String = ""
}