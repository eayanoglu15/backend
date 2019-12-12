package com.example.fourtytwo.modules.trip.response


data class HitchhikerRequestsPageResponse(
        var acceptedRequest: List<HitchhikerComingTripResponse>?=null,

        var requests: List<HitchhikerComingTripResponse>?=null,

        var tripExist: Boolean = false
)