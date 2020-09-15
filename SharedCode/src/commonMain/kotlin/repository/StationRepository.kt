package com.jetbrains.handson.mpp.mobile.repository

import com.jetbrains.handson.mpp.mobile.dataclasses.Station
import kotlin.native.concurrent.ThreadLocal

interface StationRepository {
    fun getDepartureStation(): Station?
    fun setDepartureStation(station: Station?)

    fun getArrivalStation(): Station?
    fun setArrivalStation(station: Station?)

    @ThreadLocal
    companion object : Provider<StationRepository>() {
        override fun create(): StationRepository = RepositoryProvider.getStationsRepository()
    }
}