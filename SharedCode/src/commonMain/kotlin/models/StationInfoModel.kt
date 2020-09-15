package com.jetbrains.handson.mpp.mobile.models

import kotlinx.serialization.Serializable

@Serializable
data class StationInfoModel(val name: String, var crs: String?, var nlc: String? = null) {
    init {
        // nlc used for api call by default, so if station doesn't have crs can be labelled "n/a"
        if (crs == null) { crs = "n/a" }
        // if the station doesn't have an nlc, it's crs can be used in its place in the API call
        // (all stations must have either an nlc or a crs)
        if (nlc == null) { nlc = crs }
    }
}