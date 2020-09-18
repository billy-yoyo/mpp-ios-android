package com.jetbrains.handson.mpp.mobile

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jetbrains.handson.mpp.mobile.dataclasses.Journey
import com.jetbrains.handson.mpp.mobile.dataclasses.Ticket
import com.jetbrains.handson.mpp.mobile.util.DateFormatHelper

class JourneyInfoActivity : AppCompatActivity(), JourneyInfoContract.View  {
    // Requirements for Recycler view
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: CustomJourneyAdapter

    // Requirements for updating subtitle
    private lateinit var subtitle: TextView
    private var presenter: JourneyInfoPresenter = JourneyInfoPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journey)

        presenter.onViewTaken(this)
    }



    override fun setJourney(journey: Journey) {
        subtitle = findViewById(R.id.subheading)
        subtitle.text = this.resources.getString(
            R.string.tickets_subtitle,
            DateFormatHelper.formatTime(journey.departureTime),
            DateFormatHelper.formatDate(journey.departureTime)
        )
    }

    override fun setTickets(tickets: List<Ticket>) {
        recyclerView = findViewById(R.id.tickets_recycler)
        viewAdapter = CustomJourneyAdapter(tickets, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = viewAdapter
    }
}