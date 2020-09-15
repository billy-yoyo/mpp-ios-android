package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable


class MainActivity : AppCompatActivity(), ApplicationContract.View {
    // Requirements for Recycler view
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: CustomListAdapter

    // Connection to presenter
    private var presenter: ApplicationPresenter = ApplicationPresenter()

    // Spinners
    private lateinit var spinnerDep: Spinner
    private lateinit var spinnerArr: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.main_text).text = getString(R.string.app_title)

        spinnerDep = findViewById(R.id.departure_station)
        spinnerArr = findViewById(R.id.arrival_station)
        recyclerView = findViewById(R.id.journey_recycler)

        presenter.onViewTaken(this)

        viewAdapter = CustomListAdapter(listOf(), this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = viewAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
    }

    override fun setStations(stations: List<Station>) {
        // Create an ArrayAdapter using the string array and the custom text formatting
        val adapter: ArrayAdapter<Station> = ArrayAdapter<Station>(
            applicationContext,
            R.layout.spinner_item,
            stations
        )

        // Set the drop down view to use a default spinner item on top of the custom text format
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDep.adapter = adapter
        spinnerArr.adapter = adapter

        spinnerDep.onItemSelectedListener =
            UpdatePresenterStationListener(true, presenter, adapter)
        spinnerArr.onItemSelectedListener =
            UpdatePresenterStationListener(false, presenter, adapter)
    }

    override fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.alert_title)
        builder.setMessage(message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

    override fun openUrl(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun setJourneys(journeys: List<JourneyInfo>) {
        viewAdapter = CustomListAdapter(journeys, this)
        viewAdapter.setOnItemClickListener { it -> presenter.onViewJourney(it)}
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = viewAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
    }

    fun buttonClick(view: View) {
        presenter.onTimesRequested()
    }

    override fun openJourneyView(journey: JourneyInfo, tickets: List<TicketInfo>) {
        val bundle = Bundle()

        bundle.putSerializable(JourneyInfoActivity.JOURNEY, JourneyInfoTransit.fromJourneyInfo(journey))
        bundle.putSerializable(JourneyInfoActivity.TICKETS, tickets.map { TicketInfoTransit.fromTicketInfo(it) } as ArrayList<TicketInfoTransit>)

        val intent = Intent(this, JourneyInfoActivity::class.java).apply {
            putExtras(bundle)
        }
        startActivity(intent)
    }

    class UpdatePresenterStationListener(
        private val isDeparture: Boolean,
        private val presenter: ApplicationPresenter,
        private val adapter: ArrayAdapter<Station>
    ) : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, item: View?, position: Int, id: Long) {
            if (isDeparture) {
                presenter.setDepartureStation(adapter.getItem(position))
            } else {
                presenter.setArrivalStation(adapter.getItem(position))
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            if (isDeparture) {
                presenter.setDepartureStation(null)
            } else {
                presenter.setArrivalStation(null);
            }
        }
    }
}