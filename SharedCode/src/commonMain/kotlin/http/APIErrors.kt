package com.jetbrains.handson.mpp.mobile.http

enum class APIErrors(
    val message: String
) {
    RequestError("Failed to fetch train times."),
    SameStation("The departure station cannot be the same as the destination.")
}