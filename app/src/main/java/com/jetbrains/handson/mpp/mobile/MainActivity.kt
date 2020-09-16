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
    private lateinit var progressBar: ProgressBar

    // Connection to presenter
    private var presenter: ApplicationPresenter = ApplicationPresenter()

    // Autocomplete text
    private lateinit var autotextDep: AutoCompleteTextView
    private lateinit var autotextArr: AutoCompleteTextView

    // List of stations
    private lateinit var stations: List<Station>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.main_text).text = getString(R.string.app_title)

        autotextDep = findViewById(R.id.departure_station)
        autotextArr = findViewById(R.id.arrival_station)
        recyclerView = findViewById(R.id.journey_recycler)
        progressBar = findViewById(R.id.progress_bar)

        progressBar.visibility = View.GONE

        presenter.onViewTaken(this)

        viewAdapter = CustomListAdapter(listOf(), this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = viewAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
    }

    override fun setStations(stations: List<Station>) {
        this.stations = stations

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
        
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    fun buttonClick(view: View) {
        val depText = autotextDep.text.toString()
        val arrText = autotextArr.text.toString()
        val depStation: Station?
        val arrStation: Station?

        if (depText == "") {
            showAlert("Please enter a departure station")
            return
        }
        if (arrText == "") {
            showAlert("Please enter an arrival station")
            return
        }
        if (depText != (autotextDep.onItemClickListener as UpdatePresenterStationListener).depName) {
            depStation = overrideStation(depText)
            if (depStation == null) {
                showAlert("Please select a valid departure station")
                return
            }
            presenter.setDepartureStation(depStation)
        }
        if (arrText != (autotextArr.onItemClickListener as UpdatePresenterStationListener).arrName) {
            arrStation = overrideStation(arrText)
            if (arrStation == null) {
                showAlert("Please select a valid arrival station")
                return
            }
            presenter.setArrivalStation(arrStation)
        }

        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        presenter.onTimesRequested()
    }

    private fun overrideStation(text: String) : Station? {
        val index = stations.map { it.toString().toLowerCase() }.indexOf(text.toLowerCase())
        return if (index == -1) null else stations[index]
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
        // Track dropdown selections
        var depName: String = ""
        var arrName: String = ""

        override fun onItemClick(parent: AdapterView<*>?, item: View?, position: Int, id: Long) {
            if (isDeparture) {
                presenter.setDepartureStation(adapter.getItem(position))
                depName = adapter.getItem(position).toString()
            } else {
                presenter.setArrivalStation(adapter.getItem(position))
                arrName = adapter.getItem(position).toString()
            }
        }
    }
}