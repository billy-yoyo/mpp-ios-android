package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface JourneyInfoContract {
    interface View {
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: JourneyInfoContract.View)
    }
}