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
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.SpannableStringBuilder
import android.view.MenuItem
import arrow.core.fix
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.flow.model.FlowEvent
import com.josedlpozo.galileo.flow.model.CreatedModel
import com.josedlpozo.galileo.flow.model.toModel
import com.josedlpozo.galileo.parent.extensions.padding
import com.josedlpozo.galileo.parent.extensions.tint
import com.josedlpozo.galileo.parent.extensions.toBold
import kotlinx.android.synthetic.main.galileo_event_detail_activity.textEvent
import kotlinx.android.synthetic.main.galileo_event_detail_activity.textItems

internal class ActivityEventDetailActivity : AppCompatActivity() {

    companion object {
        private const val ARG_EVENT_ID = "event_id"

        fun start(context: Context, id: Long) = Intent(context, ActivityEventDetailActivity::class.java).apply {
            putExtra(ARG_EVENT_ID, id)
        }.also(context::startActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.galileo_event_detail_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val eventId = intent.getLongExtra(ARG_EVENT_ID, 0)

        FlowEventTry.useCase.get(eventId).fix().fold({
            finish()
        }, {
            render(it)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when {
        item.itemId == android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun render(event: FlowEvent) = with(event.toModel()) {
        title = activityName
        textEvent.text = name
        textEvent.setBackgroundDrawable(resources.getDrawable(background).tint(resources.getColor(color)))
        textEvent.padding(R.dimen.galileo_margin_small)

        val values = when (this) {
            is CreatedModel -> {
                val spannable = SpannableStringBuilder()
                extras.map {
                    spannable.append(SpannableStringBuilder("${it.key} -> ${it.value}\n\n").toBold(it.key))
                }

                spannable
            }
            else -> SpannableStringBuilder()
        }

        textItems.text = values
    }
}