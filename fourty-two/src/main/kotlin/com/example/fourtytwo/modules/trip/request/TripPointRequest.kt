package com.example.fourtytwo.modules.trip.request

data class TripPointRequest(
        var tripId: Long,
        var point: Int,
        var isDriver: Boolean
)