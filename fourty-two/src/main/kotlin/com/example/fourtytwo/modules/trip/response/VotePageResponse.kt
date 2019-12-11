package com.example.fourtytwo.modules.trip.response


data class VotePageResponse(
        var votedTrip: List<DriverComingTripRequests>?=null,

        var nonVotedTrip: List<DriverComingTripRequests>?=null,

        var tripExist: Boolean = false
)