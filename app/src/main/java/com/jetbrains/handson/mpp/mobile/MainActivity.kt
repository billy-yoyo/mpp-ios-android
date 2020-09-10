package com.jetbrains.handson.mpp.mobile

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), ApplicationContract.View {

    private var presenter: ApplicationPresenter = ApplicationPresenter()
    private var spinnerDep: Spinner? = null
    private var spinnerArr: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerDep = findViewById(R.id.departure_station)
        spinnerArr = findViewById(R.id.arrival_station)

        presenter.onViewTaken(this)
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
    }

    override fun setLabel(text: String) {
        findViewById<TextView>(R.id.main_text).text = text
    }

    override fun showAlert(message: String) {
        TODO("Not yet implemented")
    }

    override fun getDepartureStation(): Station {
        return spinnerDep.selectedItem as Station
    }

    override fun getArrivalStation(): Station {
        return spinnerArr.selectedItem as Station
    }

    fun buttonClick(view: View) {
        presenter.onTimesRequested()
    }
}