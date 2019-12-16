package com.example.fourtytwo.modules.trip.model

import com.example.fourtytwo.modules.users.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface TripRepository : JpaRepository<Trip, Long> {

  fun findAllByEndTimeGreaterThanAndAvailableSeatNumberGreaterThan(endTime:Instant,availableSeat:Int = 0): List<Trip>

  fun findFirstByDriverNameOrderByEndTimeDesc(driverName:User): Trip?

  fun existsTripByDriverNameAndEndTimeGreaterThanEqual(driverName:User,endTime: Instant): Boolean



  fun findAllByEndTimeLessThanAndDriverName(endTime: Instant,driverName: User): List<Trip>





}