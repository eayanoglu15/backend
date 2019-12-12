package com.example.fourtytwo.modules.trip.response


data class VotePageResponseByDriver(
        var votedTrip: List<DriverComingTripResponse>?=null,

        var nonVotedTrip: List<DriverComingTripResponse>?=null,

        var tripExist: Boolean = false
)