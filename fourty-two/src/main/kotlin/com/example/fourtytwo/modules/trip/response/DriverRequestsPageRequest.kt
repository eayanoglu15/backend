package com.example.fourtytwo.modules.trip.response

import com.example.fourtytwo.modules.trip.model.Trip
import java.time.Instant

data class DriverRequestsPageRequest(
        var acceptedRequest: List<DriverComingTripRequests>?=null,

        var requests: List<DriverComingTripRequests>?=null,

        var tripExist: Boolean = false
)