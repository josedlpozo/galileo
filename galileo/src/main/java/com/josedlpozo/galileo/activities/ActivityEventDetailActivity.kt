package com.josedlpozo.galileo.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import arrow.core.fix
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.activities.model.ActivityEvent
import com.josedlpozo.galileo.activities.model.Created
import com.josedlpozo.galileo.activities.model.SavedInstanceState
import kotlinx.android.synthetic.main.activity_event_detail.*

class ActivityEventDetailActivity : AppCompatActivity() {

    companion object {
        private const val ARG_EVENT_ID = "event_id"

        fun start(context: Context, id: Long) = Intent(context, ActivityEventDetailActivity::class.java).apply {
            putExtra(ARG_EVENT_ID, id)
        }.also(context::startActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val eventId = intent.getLongExtra(ARG_EVENT_ID, 0)

        ActivityEventTry.useCase.get(eventId).fix().fold({
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

    private fun render(event: ActivityEvent) {
        title = event.activityName

        textEvent.text = event.name

        val values = when (event) {
            is Created -> event.extras.map {
                "${it.key} -> ${it.value}"
            }.joinToString("\n\n")
            is SavedInstanceState -> event.extras.map {
                "${it.key} -> ${it.value}"
            }.joinToString("\n\n")
            else -> ""
        }

        textItems.text = values
    }
}
