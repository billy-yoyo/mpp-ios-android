package com.jetbrains.handson.mpp.mobile

data class Station(
    val name: String,
    val id: String
) {
    override fun toString(): String {
        return name
    }
}