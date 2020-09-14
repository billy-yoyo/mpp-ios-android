package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class JourneyInfoPresenter : JourneyInfoContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    private lateinit var view: JourneyInfoContract.View

    override fun onViewTaken(view: JourneyInfoContract.View) {
        // be happy
        this.view = view
    }

    override fun onBack() {
        TODO("Not yet implemented")
    }
}