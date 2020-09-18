package com.jetbrains.handson.mpp.mobile.models

import kotlinx.serialization.Serializable

@Serializable
data class StationListModel(
    val stations: List<StationInfoModel>
)