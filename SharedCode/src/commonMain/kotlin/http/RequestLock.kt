package com.jetbrains.handson.mpp.mobile.http

import com.soywiz.klock.DateTime
import kotlin.random.Random

class RequestLock {
    class RequestKey(val id: String)

    private var key: RequestKey? = null
    private var rng = Random(DateTime.nowUnixLong())

    private fun createRandomKey(): RequestKey {
        val key = "${DateTime.nowUnixLong()}::${rng.nextInt()}"

        return RequestKey(key)
    }

    private fun claim(): RequestKey {
        key = createRandomKey()
        return key!!
    }

    private fun check(key: RequestKey): Boolean {
        return key.id == this.key?.id
    }

    /**
     * Attempts to run a function and return its result, but will return null
     * if another function is started using this lock before this one finishes.
     *
     * This is useful if you have a scenario where you're making repeated potentially
     * overlapping network requests and only ever want the latest one to complete.
     *
     * @param T the return type of the higher order function
     * @property func the function we are attempting to run
     * @return the result of func, if it completes before another func is attempted, else null
     */
    suspend fun <T> attempt(func: suspend () -> T): T? {
        val key = claim()
        val result = func()

        return if (check(key)) result else null
    }
}