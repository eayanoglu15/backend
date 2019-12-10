package com.example.fourtytwo.modules.trip.response

import java.time.Instant

data class DriverComingTripRequests(

        var from: String? = null,

        var to: String? = null,

        var startTime: Instant? = null,

        var endTime: Instant? = null,

        var id: Long? = null,

        var hitchHikerUserName: String? = null,

        var rating: Double = 0.0
)