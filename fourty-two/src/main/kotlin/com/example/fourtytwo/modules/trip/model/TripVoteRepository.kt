package com.example.fourtytwo.modules.trip.model

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TripVoteRepository : JpaRepository<TripVote, Long> {

    fun findAllByTripRequestId_Trip(tripList: List<Trip>): List<TripVote>

}
