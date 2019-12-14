package com.example.fourtytwo.modules.trip.model

import com.example.fourtytwo.modules.users.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import java.util.*


interface TripRequestRepository : JpaRepository<TripRequest, Long> {

    fun findAllByTripAndShowableTrue(trip: Trip): List<TripRequest>
    fun findOneById(tripRequestId: Long): TripRequest?

    fun findAllByPersonRequested(hitchHiker: User): List<TripRequest>





}