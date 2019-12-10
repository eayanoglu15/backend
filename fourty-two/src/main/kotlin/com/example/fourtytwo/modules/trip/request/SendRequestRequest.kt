package com.example.fourtytwo.modules.trip.request

import java.time.Instant

data class SendRequestRequest(
        var tripId: Long? = null,
        var hitchHikerUserName: String? = null
)