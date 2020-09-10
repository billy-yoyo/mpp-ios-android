package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private var view: ApplicationContract.View? = null
    private val job: Job = SupervisorJob()
    private val stations: List<Station> = listOf(
        Station("Kings Cross", "KGX"),
        Station("York", "YRK"),
        Station("Edinburgh Waverley", "EDB"),
        Station("Leeds", "LDS"),
        Station("Cattal", "CTL")
    )

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setLabel(createApplicationScreenMessage())
        view.setStations(stations);
    }

    override fun onTimesRequested() {
        view?.setLabel("Button was tapped");
    }
}
