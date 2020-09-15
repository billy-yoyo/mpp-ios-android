package com.jetbrains.handson.mpp.mobile.repository.impl

import com.jetbrains.handson.mpp.mobile.models.FaresModel
import com.jetbrains.handson.mpp.mobile.repository.FaresRepository

class FaresRepositoryImpl : FaresRepository {
    private var faresModel: FaresModel? = null

    override fun getFares(): FaresModel? {
        return faresModel
    }

    override fun setFares(model: FaresModel?) {
        faresModel = model
    }
}