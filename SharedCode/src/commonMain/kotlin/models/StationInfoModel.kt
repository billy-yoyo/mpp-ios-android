package com.jetbrains.handson.mpp.mobile.models

import kotlinx.serialization.Serializable

@Serializable
data class StationInfoModel(val name: String, var crs: String? = null, var nlc: String? = null)