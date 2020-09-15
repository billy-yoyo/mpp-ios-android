package com.jetbrains.handson.mpp.mobile.repository.impl

import com.jetbrains.handson.mpp.mobile.Station
import com.jetbrains.handson.mpp.mobile.repository.StationRepository

class StationRepositoryImpl : StationRepository {
    private var departureStation: Station? = null
    private var arrivalStation: Station? = null

    override fun getDepartureStation(): Station? {
        return departureStation
    }

    override fun setDepartureStation(station: Station?) {
        departureStation = station
    }

    override fun getArrivalStation(): Station? {
        return arrivalStation
    }

    override fun setArrivalStation(station: Station?) {
        arrivalStation = station
    }
}