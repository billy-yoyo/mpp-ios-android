package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity(), ApplicationContract.View {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: CustomListAdapter

    private var presenter: ApplicationPresenter = ApplicationPresenter()
    private lateinit var spinnerDep: Spinner
    private lateinit var spinnerArr: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerDep = findViewById(R.id.departure_station)
        spinnerArr = findViewById(R.id.arrival_station)

        presenter.onViewTaken(this)

        recyclerView = findViewById(R.id.ticket_recycler)
        viewAdapter = CustomListAdapter(listOf("Test 1", "Test 2", "Test 3", "Test 4", "Test 5", "Test 6", "Test 7", "Test 8"))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = viewAdapter
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

    override fun setLabel(text: String) {
        findViewById<TextView>(R.id.main_text).text = text
    }

    override fun showAlert(message: String) {
        TODO("Not yet implemented")
    }

    override fun openUrl(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun addTicket(ticket: TicketInfo) {
        //TODO("Not yet implemented")
    }

    override fun clearTickets() {
        //TODO("Not yet implemented")
    }

    fun buttonClick(view: View) {
        presenter.onTimesRequested()
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