package com.jetbrains.handson.mpp.mobile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
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
    private lateinit var autotextDeparture: AutoCompleteTextView
    private lateinit var autotextArrival: AutoCompleteTextView

    // List of stations
    private lateinit var stations: List<Station>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.main_text).text = getString(R.string.app_title)

        autotextDeparture = findViewById(R.id.departure_station)
        autotextArrival = findViewById(R.id.arrival_station)
        recyclerView = findViewById(R.id.journey_recycler)
        progressBar = findViewById(R.id.progress_bar)

        progressBar.visibility = View.GONE

        viewAdapter = CustomListAdapter(listOf(), this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = viewAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))

        presenter.onViewTaken(this)
    }

    override fun setStations(stations: List<Station>) {
        this.stations = stations

        val adapter: ArrayAdapter<Station> = ArrayAdapter<Station>(
            applicationContext,
            R.layout.autocomplete_item,
            stations
        )

        adapter.setDropDownViewResource(R.layout.autocomplete_item)
        autotextDeparture.setAdapter(adapter)
        autotextArrival.setAdapter(adapter)

        autotextDeparture.onItemClickListener =
            UpdatePresenterStationListener(true, presenter, adapter, this)
        autotextArrival.onItemClickListener =
            UpdatePresenterStationListener(false, presenter, adapter, this)
    }

    override fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.alert_title)
        builder.setMessage(message)
        builder.setIcon(R.drawable.alert_icon)
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
        val departureText = autotextDeparture.text.toString()
        val arrivalText = autotextArrival.text.toString()
        val departureStation: Station?
        val arrivalStation: Station?

        closeKeyboard()

        if (departureText == "") {
            showAlert("Please enter a departure station.")
            return
        }
        if (arrivalText == "") {
            showAlert("Please enter an arrival station.")
            return
        }
        if (departureText != (autotextDeparture.onItemClickListener as UpdatePresenterStationListener).departureName) {
            departureStation = overrideStation(departureText)
            if (departureStation == null) {
                showAlert("Please select a valid departure station.")
                return
            }
            presenter.setDepartureStation(departureStation)
        }
        if (arrivalText != (autotextArrival.onItemClickListener as UpdatePresenterStationListener).arrivalName) {
            arrivalStation = overrideStation(arrivalText)
            if (arrivalStation == null) {
                showAlert("Please select a valid arrival station.")
                return
            }
            presenter.setArrivalStation(arrivalStation)
        }

        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        presenter.onTimesRequested()
    }

    private fun overrideStation(text: String) : Station? {
        val index = stations.map { it.toString().trim().toLowerCase() }.indexOf(text.trim().toLowerCase())
        return if (index == -1) null else stations[index]
    }

    override fun openJourneyView() {
        val intent = Intent(this, JourneyInfoActivity::class.java)
        startActivity(intent)
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    class UpdatePresenterStationListener(
        private val isDeparture: Boolean,
        private val presenter: ApplicationPresenter,
        private val adapter: ArrayAdapter<Station>,
        private val context: MainActivity
    ) : AdapterView.OnItemClickListener {
        // Track dropdown selections
        var departureName: String = ""
        var arrivalName: String = ""

        override fun onItemClick(parent: AdapterView<*>?, item: View?, position: Int, id: Long) {
            if (isDeparture) {
                presenter.setDepartureStation(adapter.getItem(position))
                departureName = adapter.getItem(position).toString()
            } else {
                presenter.setArrivalStation(adapter.getItem(position))
                arrivalName = adapter.getItem(position).toString()
            }
            context.closeKeyboard()
        }

        private fun closeKeyboard() {
            val view = context.currentFocus
            if (view != null) {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}