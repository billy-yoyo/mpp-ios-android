package com.jetbrains.handson.mpp.mobile

import com.jetbrains.handson.mpp.mobile.repository.JourneyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class JourneyInfoPresenter : JourneyInfoContract.Presenter() {

    private val journeyRepository by JourneyRepository.lazyGet()

    private val dispatchers = AppDispatchersImpl()
    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    private lateinit var view: JourneyInfoContract.View

    override fun onViewTaken(view: JourneyInfoContract.View) {
        this.view = view

        view.setJourney(journeyRepository.getJourney()!!)
        view.setTickets(journeyRepository.getTickets())
    }
}