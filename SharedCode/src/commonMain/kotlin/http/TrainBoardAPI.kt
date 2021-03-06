package com.jetbrains.handson.mpp.mobile.http

import com.jetbrains.handson.mpp.mobile.dataclasses.Station
import com.jetbrains.handson.mpp.mobile.models.FaresModel
import com.jetbrains.handson.mpp.mobile.models.StationListModel
import com.jetbrains.handson.mpp.mobile.util.DateFormatHelper
import com.soywiz.klock.DateTimeTz
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*

class TrainBoardAPI(private val errorReporter: (error: APIErrors) -> Unit) {
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

    private fun handleGenericError(error: Throwable) {
        errorReporter(APIErrors.RequestError)
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

        if (departureStation.apiCode == arrivalStation.apiCode) {
            errorReporter(APIErrors.SameStation)
            return FaresModel(listOf())
        }

        url.path(Strings.version, fares.path)

        url.parameters.append(fares.origin, departureStation.apiCode)
        url.parameters.append(fares.destination, arrivalStation.apiCode)
        url.parameters.append(fares.noChanges, noChanges.toString())
        url.parameters.append(fares.adults, adults.toString())
        url.parameters.append(fares.children, children.toString())
        url.parameters.append(fares.journeyType, journeyType)

        if (outboundDateTime != null) {
            url.parameters.append(fares.outboundDateTime, DateFormatHelper.formatForInput(outboundDateTime))
            url.parameters.append(fares.outboundIsArriveBy, outboundIsArriveBy.toString())
        }

        if (inboundDateTime != null) {
            url.parameters.append(fares.inboundDateTime, DateFormatHelper.formatForInput(inboundDateTime))
            url.parameters.append(fares.inboundIsArriveBy, inboundIsArriveBy.toString())
        }

        return try {
            client.get(url.buildString())
        } catch (cause: Throwable) {
            handleGenericError(cause)
            
            FaresModel(listOf())
        }
    }

    suspend fun getStationListModel(): StationListModel {
        val url = createBuilder()
        val stations = Strings.StationUrl

        url.path(Strings.version, stations.path)

        return try {
            client.get(url.buildString())
        } catch (cause: Throwable) {
            handleGenericError(cause)

            StationListModel(listOf())
        }
    }
}