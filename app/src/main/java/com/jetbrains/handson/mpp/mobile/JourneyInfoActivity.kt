package com.jetbrains.handson.mpp.mobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class JourneyInfoActivity : AppCompatActivity(), JourneyInfoContract.View  {
    companion object {
        val JOURNEY: String = "JOURNEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val journey =
            (this.intent.extras?.getSerializable(JOURNEY) as JourneyInfoTransit).toJourneyInfo()

        setContentView(R.layout.activity_journey)
    }
}