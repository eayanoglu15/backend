package com.example.fourtytwo.modules.trip.model

import com.example.fourtytwo.modules.users.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TripVoteRepository : JpaRepository<TripVote, Long> {
    fun findOneById(id: Long): TripVote
    fun findAllByTripRequestId_Trip(trip: Trip): List<TripVote>
    fun findAllByTripRequestId_PersonRequested(hitchHiker: User): List<TripVote>
}
