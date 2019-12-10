package com.example.fourtytwo.modules.trip.response

import java.time.Instant

data class TripResponse(

        var from: String? = null,

        var to: String? = null,

        var startTime: Instant? = null,

        var endTime: Instant? = null,

        var availableSeatNumber: Int = 0,

        var id: Long? = null,

        var driverUserName: String? = null,

        var carModel: String? = null,

        var rating: Double = 0.0
)