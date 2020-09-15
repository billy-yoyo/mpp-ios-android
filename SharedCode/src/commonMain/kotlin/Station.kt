package com.jetbrains.handson.mpp.mobile

data class Station(
    val name: String,
    val crs: String?,
    val nlc: String?
) {
    // A station must have either a crs or nlc code, but not necessarily both
    val apiCode: String = crs ?: nlc!!

    override fun toString(): String {
        return name
    }
}