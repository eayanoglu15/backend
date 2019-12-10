package com.example.fourtytwo.modules.trip.service

import com.example.fourtytwo.modules.shared.RestException
import com.example.fourtytwo.modules.trip.model.Trip
import com.example.fourtytwo.modules.trip.model.TripRepository
import com.example.fourtytwo.modules.trip.model.TripRequest
import com.example.fourtytwo.modules.trip.model.TripRequestRepository
import com.example.fourtytwo.modules.trip.model.TripVote
import com.example.fourtytwo.modules.trip.model.TripVoteRepository
import com.example.fourtytwo.modules.trip.request.AcceptTripRequest
import com.example.fourtytwo.modules.trip.request.CreateTripRequest
import com.example.fourtytwo.modules.trip.request.DriverTripCheckRequest
import com.example.fourtytwo.modules.trip.request.SendRequestRequest
import com.example.fourtytwo.modules.trip.response.DriverComingTripRequests
import com.example.fourtytwo.modules.trip.response.DriverRequestsPageRequest
import com.example.fourtytwo.modules.trip.response.TripResponse
import com.example.fourtytwo.modules.users.model.User
import com.example.fourtytwo.modules.users.model.UserRepository
import com.example.fourtytwo.modules.users.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneOffset


@Service
class TripService @Autowired constructor(
        private val userRepository: UserRepository,
        private val tripRepository: TripRepository,
        private val tripVoteRepository: TripVoteRepository,
        private val tripRequestRepository: TripRequestRepository,
        private val userService: UserService

) {
    fun getAllAvailableTrips(): List<TripResponse> {
        val now = LocalDateTime.now().toInstant(ZoneOffset.UTC)
        return tripRepository.findAllByEndTimeGreaterThanAndAvailableSeatNumberGreaterThan(now).map { trip ->
            TripResponse(
                    from = trip.fromWhere,
                    to = trip.toWhere,
                    startTime = trip.startTime,
                    endTime = trip.endTime,
                    availableSeatNumber = trip.availableSeatNumber,
                    id = trip.id,
                    driverUserName = trip.driverName!!.username,
                    carModel = trip.driverName!!.carModel,
                    rating = trip.driverName!!.point
            )
        }.toList()
    }

    fun getDriversAllRequests(driverTripCheckRequest: DriverTripCheckRequest): DriverRequestsPageRequest {
        val now = LocalDateTime.now().toInstant(ZoneOffset.UTC)
        if (!userService.existsByUsername(driverTripCheckRequest.driverUserName)) {
            throw RestException(
                    "Exception.notFound",
                    HttpStatus.UNAUTHORIZED,
                    "User",
                    driverTripCheckRequest.driverUserName
            )
        } else {
            var driver = userService.getUserByUsername(driverTripCheckRequest.driverUserName)
            if(!tripRepository.existsTripByDriverNameAndEndTimeGreaterThanEqual(driver,now)){
                return DriverRequestsPageRequest(
                        tripExist = false
                )

            }else{
                var trip =tripRepository.findFirstByDriverNameOrderByEndTimeDesc(driver)
                if(trip==null){
                    return DriverRequestsPageRequest(
                            tripExist = false
                    )

                }else{
                    val allRequests= tripRequestRepository.findAllByTripAndShowableTrue(trip)
                    return DriverRequestsPageRequest(
                            requests = allRequests.filter { !it.isAccepted }.map {
                                DriverComingTripRequests(
                                        from = it.trip!!.fromWhere,
                                        to = it.trip!!.toWhere,
                                        startTime = it.trip!!.startTime,
                                        endTime = it.trip!!.endTime,
                                        id = it.id,
                                        hitchHikerUserName = it.personRequested!!.username,
                                        rating = it.personRequested!!.point
                                )
                            }.toList(),
                            acceptedRequest = allRequests.filter { it.isAccepted }.map {
                                DriverComingTripRequests(
                                        from = it.trip!!.fromWhere,
                                        to = it.trip!!.toWhere,
                                        startTime = it.trip!!.startTime,
                                        endTime = it.trip!!.endTime,
                                        id = it.id,
                                        hitchHikerUserName = it.personRequested!!.username,
                                        rating = it.personRequested!!.point
                                )
                            }.toList(),
                            tripExist = true
                    )
                }
            }
        }
    }

    @Transactional
    fun createTrip(createTripRequest: CreateTripRequest){
        var trip:Trip= Trip()
        trip.totalSeatNumber=createTripRequest.totalSeatNumber
        trip.availableSeatNumber=createTripRequest.totalSeatNumber
        trip.endTime=createTripRequest.endTime
        trip.startTime=createTripRequest.startTime
        trip.fromWhere=createTripRequest.from
        trip.toWhere=createTripRequest.to
        trip.driverName = userService.getUserByUsername(createTripRequest.driverUserName!!)
        tripRepository.save(trip)
    }

    @Transactional
    fun acceptRequest(acceptRequest: AcceptTripRequest){
        val tripRequests= tripRequestRepository.findOneById(acceptRequest.tripRequestId!!)
        if(tripRequests== null){
           throw RestException(
                    "Exception.notFound",
                    HttpStatus.UNAUTHORIZED,
                    "User",
                    acceptRequest.tripRequestId!!
            )
        }
        tripRequests.isAccepted=true
        tripRequestRepository.save(
                tripRequests
        )
        var tripVote=TripVote()
        tripVote.tripRequestId=tripRequests
        tripVoteRepository.save(
                tripVote
        )
        var trip = tripRequests.trip
        trip!!.availableSeatNumber=trip!!.availableSeatNumber-1
        tripRepository.save(trip)
    }

    @Transactional
    fun declineRequest(declineRequest: AcceptTripRequest){
        val tripRequests= tripRequestRepository.findOneById(declineRequest.tripRequestId!!)
        if(tripRequests== null){
            throw RestException(
                    "Exception.notFound",
                    HttpStatus.UNAUTHORIZED,
                    "User",
                    declineRequest.tripRequestId!!
            )
        }
        tripRequests.showable=false
        tripRequestRepository.save(
                tripRequests
        )
    }


    @Transactional
    fun requestJoinTrip(requestJoinTripRequest: SendRequestRequest){
       val trip = tripRepository.findById(requestJoinTripRequest.tripId!!)
        val hitchHiker: User = userService.getUserByUsername(requestJoinTripRequest.hitchHikerUserName!!)

        var tripRequest=TripRequest()
        tripRequest.personRequested=hitchHiker
        tripRequest.trip=trip.map { it }.orElseThrow {
            RestException(
                    "Exception.notFound",
                    HttpStatus.UNAUTHORIZED,
                    "User",
                    requestJoinTripRequest.tripId!!
            )
        }
        tripRequestRepository.save(tripRequest)
    }

}

