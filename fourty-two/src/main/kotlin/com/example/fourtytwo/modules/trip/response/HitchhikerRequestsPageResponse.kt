package com.example.fourtytwo.modules.trip.response


data class HitchhikerRequestsPageResponse(
        var acceptedRequests: List<HitchhikerComingTripResponse> =  emptyList(),

        var waitingRequests: List<HitchhikerComingTripResponse> =  emptyList(),

        var rejectedRequests: List<HitchhikerComingTripResponse> =  emptyList(),

        var tripExist: Boolean = false
)