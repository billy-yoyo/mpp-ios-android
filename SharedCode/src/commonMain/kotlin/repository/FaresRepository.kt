package com.jetbrains.handson.mpp.mobile.repository

import com.jetbrains.handson.mpp.mobile.models.FaresModel
import kotlin.native.concurrent.ThreadLocal

interface FaresRepository {
    fun getFares(): FaresModel?
    fun setFares(model: FaresModel?)

    @ThreadLocal
    companion object : Provider<FaresRepository>() {
        override fun create(): FaresRepository = RepositoryProvider.getFaresRepository()
    }
}