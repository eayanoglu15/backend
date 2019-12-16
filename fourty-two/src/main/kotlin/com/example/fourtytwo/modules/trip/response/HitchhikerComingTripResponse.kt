package com.example.fourtytwo.modules.trip.response

import java.time.Instant


data class HitchhikerComingTripResponse(

        var from: String? = null,

        var to: String? = null,

        var startTime: Instant? = null,

        var endTime: Instant? = null,

        var id: Long? = null,

        var driverUserName: String? = null,

        var rating: Double = 0.0,

        var carModel: String? = null,

        var showable: Boolean = true,
        var image: String?,
        val voteGiven: Int?
)