package com.jetbrains.handson.mpp.mobile

data class Station(
    val name: String,
    val crs: String,
    val nlc: String
) {
    override fun toString(): String {
        return name
    }
}