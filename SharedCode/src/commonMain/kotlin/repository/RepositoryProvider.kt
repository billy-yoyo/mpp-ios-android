package com.jetbrains.handson.mpp.mobile.repository

import com.jetbrains.handson.mpp.mobile.repository.impl.FaresRepositoryImpl
import com.jetbrains.handson.mpp.mobile.repository.impl.JourneyRepositoryImpl
import com.jetbrains.handson.mpp.mobile.repository.impl.StationRepositoryImpl
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object RepositoryProvider {
    fun getStationsRepository(): StationRepository = StationRepositoryImpl()
    fun getJourneyRepository(): JourneyRepository = JourneyRepositoryImpl()
    fun getFaresRepository(): FaresRepository = FaresRepositoryImpl()
}