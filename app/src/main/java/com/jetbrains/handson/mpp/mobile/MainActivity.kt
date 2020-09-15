package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jetbrains.handson.mpp.mobile.dataclasses.Journey
import com.jetbrains.handson.mpp.mobile.dataclasses.Station


class MainActivity : AppCompatActivity(), ApplicationContract.View {
    // Requirements for Recycler view
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: CustomListAdapter

    // Connection to presenter
    private var presenter: ApplicationPresenter = ApplicationPresenter()

    // Autocomplete text
    private lateinit var autotextDep: AutoCompleteTextView
    private lateinit var autotextArr: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.main_text).text = getString(R.string.app_title)

        autotextDep = findViewById(R.id.departure_station)
        autotextArr = findViewById(R.id.arrival_station)
        recyclerView = findViewById(R.id.journey_recycler)

        presenter.onViewTaken(this)

        viewAdapter = CustomListAdapter(listOf(), this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = viewAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
    }

    override fun setStations(stations: List<Station>) {
        val adapter: ArrayAdapter<Station> = ArrayAdapter<Station>(
            applicationContext,
            R.layout.autocomplete_item,
            stations
        )

        adapter.setDropDownViewResource(R.layout.autocomplete_item)
        autotextDep.setAdapter(adapter)
        autotextArr.setAdapter(adapter)

        autotextDep.onItemClickListener =
            UpdatePresenterStationListener(true, presenter, adapter)
        autotextArr.onItemClickListener =
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

    override fun setJourneys(journeys: List<Journey>) {
        viewAdapter = CustomListAdapter(journeys, this)
        viewAdapter.setOnItemClickListener { it -> presenter.onViewJourney(it)}
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = viewAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
    }

    fun buttonClick(view: View) {
        presenter.onTimesRequested()
    }

    override fun openJourneyView() {
        val intent = Intent(this, JourneyInfoActivity::class.java)
        startActivity(intent)
    }

    class UpdatePresenterStationListener(
        private val isDeparture: Boolean,
        private val presenter: ApplicationPresenter,
        private val adapter: ArrayAdapter<Station>
    ) : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, item: View?, position: Int, id: Long) {
            if (isDeparture) {
                presenter.setDepartureStation(adapter.getItem(position))
            } else {
                presenter.setArrivalStation(adapter.getItem(position))
            }
        }
    }
}