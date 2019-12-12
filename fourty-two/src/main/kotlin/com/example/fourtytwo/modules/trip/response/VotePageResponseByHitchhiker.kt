package com.example.fourtytwo.modules.trip.response


data class VotePageResponseByHitchhiker(
        var votedTrip: List<HitchhikerComingTripResponse>?=null,

        var nonVotedTrip: List<HitchhikerComingTripResponse>?=null,

        var tripExist: Boolean = false
)