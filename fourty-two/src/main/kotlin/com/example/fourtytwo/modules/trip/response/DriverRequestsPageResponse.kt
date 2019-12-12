package com.example.fourtytwo.modules.trip.response

data class DriverRequestsPageResponse(
        var acceptedRequest: List<DriverComingTripResponse>?=null,

        var requests: List<DriverComingTripResponse>?=null,

        var tripExist: Boolean = false
)