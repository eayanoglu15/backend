package com.example.fourtytwo.modules.trip.response


data class HitchhikerRequestsPageResponse(
        var acceptedRequests: List<HitchhikerComingTripResponse>?=null,

        var waitingRequests: List<HitchhikerComingTripResponse>?=null,

        var rejectedRequests: List<HitchhikerComingTripResponse>?=null,

        var tripExist: Boolean = false
)