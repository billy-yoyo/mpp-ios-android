package com.jetbrains.handson.mpp.mobile

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class JourneyInfoActivity : AppCompatActivity(), JourneyInfoContract.View  {
    companion object {
        val JOURNEY: String = "JOURNEY"
        val TICKETS: String = "TICKETS"
    }

    // Requirements for Recycler view
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: CustomJourneyAdapter

    // Requirements for updating subtitle
    private lateinit var subtitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val journey =
            (this.intent.extras?.getSerializable(JOURNEY) as JourneyInfoTransit).toJourneyInfo()

        val tickets = (this.intent.extras?.getSerializable(TICKETS) as ArrayList<*>).map { (it as TicketInfoTransit).toTicketInfo() }

        setContentView(R.layout.activity_journey)

        subtitle = findViewById(R.id.subheading)
        subtitle.text = this.resources.getString(
            R.string.tickets_subtitle,
            formatTime(journey.departureTime),
            formatDate(journey.departureTime)
        )

        recyclerView = findViewById(R.id.tickets_recycler)
        viewAdapter = CustomJourneyAdapter(tickets, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = viewAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
    }

    private fun formatTime(datetime: String): String {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZ", Locale.UK)
        val outputFormatter = SimpleDateFormat("h:mm a" , Locale.UK)
        val date = inputFormatter.parse(datetime) ?: return datetime
        return outputFormatter.format(date)
    }

    private fun formatDate(datetime: String): String {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZ", Locale.UK)
        val outputFormatter = SimpleDateFormat("MMM d" , Locale.UK)
        val date = inputFormatter.parse(datetime) ?: return datetime
        return outputFormatter.format(date)
    }
}