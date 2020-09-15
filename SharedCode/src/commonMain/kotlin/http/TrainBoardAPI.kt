package com.jetbrains.handson.mpp.mobile.http

import com.jetbrains.handson.mpp.mobile.Station
import com.jetbrains.handson.mpp.mobile.models.FaresModel
import com.jetbrains.handson.mpp.mobile.models.StationListModel
import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeTz
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*

class TrainBoardAPI {
    private object Strings {
        const val host = "mobile-api-dev.lner.co.uk"

        const val version = "v1"

        object FaresUrl {
            const val path = "fares"

            const val origin = "originStation"
            const val destination = "destinationStation"
            const val noChanges = "noChanges"
            const val adults = "numberOfAdults"
            const val children = "numberOfChildren"
            const val journeyType = "journeyType"
            const val outboundDateTime = "outboundDateTime"
            const val outboundIsArriveBy = "outboundIsArriveBy"
            const val inboundDateTime = "inboundDateTime"
            const val inboundIsArriveBy = "inboundIsArriveBy"
        }

        object StationUrl {
            const val path = "stations"
        }
    }

    private val client = HttpClient() {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {ignoreUnknownKeys=true})
        }
    }

    private fun createBuilder(): URLBuilder {
        return URLBuilder(
            URLProtocol.HTTPS,
            Strings.host,
            DEFAULT_PORT
        )
    }

    private fun formatDate(date: DateTimeTz): String {
        return date.format("YYYY-MM-dd'T'HH:mm:ss.SSSXXX")
    }

    suspend fun getFaresModel(
        departureStation: Station,
        arrivalStation: Station,
        noChanges: Boolean = false,
        adults: Int = 1,
        children: Int = 0,
        journeyType: String = "single",
        outboundDateTime: DateTimeTz? = null,
        outboundIsArriveBy: Boolean = false,
        inboundDateTime: DateTimeTz? = null,
        inboundIsArriveBy: Boolean = false
    ): FaresModel {
        val url = createBuilder()
        val fares = Strings.FaresUrl

        url.path(Strings.version, fares.path)

        url.parameters.append(fares.origin, departureStation.apiCode)
        url.parameters.append(fares.destination, arrivalStation.apiCode)
        url.parameters.append(fares.noChanges, noChanges.toString())
        url.parameters.append(fares.adults, adults.toString())
        url.parameters.append(fares.children, children.toString())
        url.parameters.append(fares.journeyType, journeyType)

        if (outboundDateTime != null) {
            url.parameters.append(fares.outboundDateTime, formatDate(outboundDateTime))
            url.parameters.append(fares.outboundIsArriveBy, outboundIsArriveBy.toString())
        }

        if (inboundDateTime != null) {
            url.parameters.append(fares.inboundDateTime, formatDate(inboundDateTime))
            url.parameters.append(fares.inboundIsArriveBy, inboundIsArriveBy.toString())
        }

        return client.get(url.buildString())
    }

    suspend fun getStationListModel(): StationListModel {
        val url = createBuilder()
        val stations = Strings.StationUrl

        url.path(Strings.version, stations.path)

        return client.get(url.buildString())
    }
}