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

    fun claim(): RequestKey {
        key = createRandomKey()
        return key!!
    }

    fun check(key: RequestKey): Boolean {
        return key.id == this.key?.id
    }

    suspend fun <T> attempt(func: suspend () -> T): T? {
        val key = claim()
        val result = func()

        return if (check(key)) result else null
    }
}