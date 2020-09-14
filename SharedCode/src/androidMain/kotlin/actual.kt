package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.Dispatchers

actual class AppDispatchersImpl: AppDispatchers {
    override val main = Dispatchers.Main
    override val io = Dispatchers.IO
}
