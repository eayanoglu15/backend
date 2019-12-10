package com.example.fourtytwo.modules.trip.request

import java.time.Instant

data class CreateTripRequest(
        var from: String? = null,

        var to: String? = null,

        var startTime: Instant? = null,

        var endTime: Instant? = null,

        var totalSeatNumber: Int = 0,

        var id: Long? = null,

        var driverUserName: String? = null
)