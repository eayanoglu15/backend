package com.example.fourtytwo.modules.trip.service

import com.example.fourtytwo.modules.shared.RestException
import com.example.fourtytwo.modules.trip.model.Trip
import com.example.fourtytwo.modules.trip.model.TripRepository
import com.example.fourtytwo.modules.trip.model.TripRequest
import com.example.fourtytwo.modules.trip.model.TripRequestRepository
import com.example.fourtytwo.modules.trip.model.TripVote
import com.example.fourtytwo.modules.trip.model.TripVoteRepository
import com.example.fourtytwo.modules.trip.request.*
import com.example.fourtytwo.modules.trip.response.*
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
    private val locationList=listOf("Hacıosman","Levent","Koç Batı","Beşiktaş","Kadıköy")
    private val distanceMap: Map<String,Map<String,Double>> = mutableMapOf<String,Map<String,Double>>(
            "Hacıosman" to mutableMapOf("Levent" to 9.2, "Koç Batı" to 8.4, "Beşiktaş" to 14.6,
                    "Kadıköy" to 23.6 ),
            "Levent" to mutableMapOf("Hacıosman" to 9.2, "Koç Batı" to 17.4, "Beşiktaş" to 5.2,
                    "Kadıköy" to 14.5 ),
            "Koç Batı" to mutableMapOf("Hacıosman" to 8.4, "Levent" to 17.4, "Beşiktaş" to 23.7,
                    "Kadıköy" to 31.9 ),
            "Beşiktaş" to mutableMapOf("Hacıosman" to 14.6, "Levent" to 5.2, "Kadıköy" to 14.5,"Koç Batı" to 23.7),
            "Kadıköy" to mutableMapOf("Hacıosman" to 23.6, "Levent" to 14.5, "Beşiktaş" to 14.5,
                    "Koç Batı" to 31.9 )
    )
    fun getAllAvailableTrips(hitchhikerTripCheck: HitchhikerTripCheckRequest): List<TripResponse> {
        val now = LocalDateTime.now().toInstant(ZoneOffset.UTC)
        val user = userService.getUserByUsername(hitchhikerTripCheck.hitchhikerUserName)
        var requestedOnes = tripRequestRepository.findAllByPersonRequested(user)
        return tripRepository.findAllByEndTimeGreaterThanAndAvailableSeatNumberGreaterThan(now).map { trip ->

            if(requestedOnes.find { it.trip == trip } == null ){
                TripResponse(
                        from = trip.fromWhere,
                        to = trip.toWhere,
                        startTime = trip.startTime,
                        endTime = trip.endTime,
                        availableSeatNumber = trip.availableSeatNumber,
                        distance = trip.distance,
                        id = trip.id,
                        driverUserName = trip.driverName!!.username,
                        carModel = trip.driverName!!.carModel
                )
            }
            else null
        }.filterNotNull().toList()
    }

    fun getFromAndToList(): TripFromToResponse {
        return TripFromToResponse(
                fromToLocationList = locationList
        )
    }

    fun getCarModel(): CarModelResponse {
        return CarModelResponse(
        )
    }



    fun getDriversAllRequests(driverTripCheckRequest: DriverTripCheckRequest): DriverRequestsPageResponse {
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
                return DriverRequestsPageResponse(
                        tripExist = false
                )

            }else{
                var trip =tripRepository.findFirstByDriverNameOrderByEndTimeDesc(driver)
                if(trip==null){
                    return DriverRequestsPageResponse(
                            tripExist = false
                    )

                }else{
                    val allRequests= tripRequestRepository.findAllByTripAndShowableTrue(trip)
                    return DriverRequestsPageResponse(
                            requests = allRequests.filter { !it.isAccepted }.map {
                                DriverComingTripResponse(
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
                                DriverComingTripResponse(
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

    fun getHitchhikersAllRequests(hitchhikerTripCheckRequest: HitchhikerTripCheckRequest): HitchhikerRequestsPageResponse {
        val now = LocalDateTime.now().toInstant(ZoneOffset.UTC)
        if (!userService.existsByUsername(hitchhikerTripCheckRequest.hitchhikerUserName)) {
            throw RestException(
                    "Exception.notFound",
                    HttpStatus.UNAUTHORIZED,
                    "User",
                    hitchhikerTripCheckRequest.hitchhikerUserName
            )
        } else {
                var hitchhiker = userService.getUserByUsername(hitchhikerTripCheckRequest.hitchhikerUserName)

                var allRequests = tripRequestRepository.findAllByPersonRequested(hitchhiker)
            for (trip in allRequests) {
                if(trip.trip!!.endTime !!< LocalDateTime.now().toInstant(ZoneOffset.UTC)){
                    trip.showable = false
                }
            }
                            return HitchhikerRequestsPageResponse(
                            waitingRequests = allRequests.filter { !it.isAccepted && it.showable}.map {
                                HitchhikerComingTripResponse(
                                        from = it.trip!!.fromWhere,
                                        to = it.trip!!.toWhere,
                                        startTime = it.trip!!.startTime,
                                        endTime = it.trip!!.endTime,
                                        id = it.id,
                                        driverUserName = it.trip!!.driverName!!.username,
                                        rating = it.trip!!.driverName!!.point,
                                        carModel = it.trip!!.driverName!!.carModel,
                                        showable = it.showable

                                )
                            }.toList(),
                            acceptedRequests = allRequests.filter { it.isAccepted }.map {
                                HitchhikerComingTripResponse(
                                        from = it.trip!!.fromWhere,
                                        to = it.trip!!.toWhere,
                                        startTime = it.trip!!.startTime,
                                        endTime = it.trip!!.endTime,
                                        id = it.id,
                                        driverUserName = it.trip!!.driverName!!.username,
                                        rating = it.trip!!.driverName!!.point,
                                        carModel = it.trip!!.driverName!!.carModel,
                                        showable = it.showable

                                )
                            }.toList(),
                            rejectedRequests = allRequests.filter { !it.isAccepted && !it.showable}.map {
                                HitchhikerComingTripResponse(
                                        from = it.trip!!.fromWhere,
                                        to = it.trip!!.toWhere,
                                        startTime = it.trip!!.startTime,
                                        endTime = it.trip!!.endTime,
                                        id = it.id,
                                        driverUserName = it.trip!!.driverName!!.username,
                                        rating = it.trip!!.driverName!!.point,
                                        carModel = it.trip!!.driverName!!.carModel,
                                        showable = it.showable
                                )
                            }.toList(),
                            tripExist = true
                    )
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
        trip.distance=distanceMap.getOrDefault(trip.fromWhere,distanceMap.get("Hacıosman"))
                ?.getOrDefault(trip.toWhere,0.0) ?: 0.0
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
        if(tripRequests.trip!!.availableSeatNumber == 0){
            throw RestException(
                    "Exception.NoSeatsAvailable!",
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

    fun driverVotePage(driverUsername:DriverTripCheckRequest):VotePageResponseByDriver{
        val driver= userService.getUserByUsername(driverUsername.driverUserName)
        val now = LocalDateTime.now().minusHours(1).toInstant(ZoneOffset.UTC)
        val trips = tripRepository.findAllByEndTimeLessThanAndDriverName(now,driver)
        val tripVote= mutableListOf<TripVote>()
        trips.map {
           tripVote.addAll(tripVoteRepository.findAllByTripRequestId_Trip(it))
        }
        return VotePageResponseByDriver(
                    votedTrip =tripVote.filter { it.voteGivenByDriver!=null }.map {
                        DriverComingTripResponse(
                                from = it.tripRequestId!!.trip!!.fromWhere,
                                to = it.tripRequestId!!.trip!!.toWhere,
                                startTime = it.tripRequestId!!.trip!!.startTime,
                                endTime = it.tripRequestId!!.trip!!.endTime,
                                id=it.id,
                                hitchHikerUserName = it.tripRequestId!!.personRequested!!.username,
                                rating = it.tripRequestId!!.personRequested!!.point
                        )
                    }.toList(),
                nonVotedTrip = tripVote.filter { it.voteGivenByDriver==null }.map {
                    DriverComingTripResponse(
                            from = it.tripRequestId!!.trip!!.fromWhere,
                            to = it.tripRequestId!!.trip!!.toWhere,
                            startTime = it.tripRequestId!!.trip!!.startTime,
                            endTime = it.tripRequestId!!.trip!!.endTime,
                            id=it.id,
                            hitchHikerUserName = it.tripRequestId!!.personRequested!!.username,
                            rating = it.tripRequestId!!.personRequested!!.point
                    )
                }.toList(),
                tripExist = !tripVote.isEmpty()
            )
    }

    fun hitchHikerVotePage(hitchhikerUsername:HitchhikerTripCheckRequest):VotePageResponseByHitchhiker{
        val hitchHiker= userService.getUserByUsername(hitchhikerUsername.hitchhikerUserName)
        val now = LocalDateTime.now().minusHours(1).toInstant(ZoneOffset.UTC)
        val tripVote = tripVoteRepository.findAllByTripRequestId_PersonRequested(hitchHiker)

        return VotePageResponseByHitchhiker(
                votedTrip =tripVote.filter { it.voteGivenByHitchhiker!=null }.map {
                    HitchhikerComingTripResponse(
                            from = it.tripRequestId!!.trip!!.fromWhere,
                            to = it.tripRequestId!!.trip!!.toWhere,
                            startTime = it.tripRequestId!!.trip!!.startTime,
                            endTime = it.tripRequestId!!.trip!!.endTime,
                            id=it.id,
                            driverUserName = it.tripRequestId!!.trip!!.driverName!!.username,
                            rating = it.tripRequestId!!.trip!!.driverName!!.point,
                            carModel = it.tripRequestId!!.trip!!.driverName!!.carModel


                    )
                }.toList(),
                nonVotedTrip = tripVote.filter { it.voteGivenByDriver==null }.map {
                    HitchhikerComingTripResponse(
                            from = it.tripRequestId!!.trip!!.fromWhere,
                            to = it.tripRequestId!!.trip!!.toWhere,
                            startTime = it.tripRequestId!!.trip!!.startTime,
                            endTime = it.tripRequestId!!.trip!!.endTime,
                            id=it.id,
                            driverUserName = it.tripRequestId!!.trip!!.driverName!!.username,
                            rating = it.tripRequestId!!.trip!!.driverName!!.point,
                            carModel = it.tripRequestId!!.trip!!.driverName!!.carModel
                    )
                }.toList(),
                tripExist = !tripVote.isEmpty()
        )
    }

    @Transactional
    fun tripPointRequest(tripPointRequest: TripPointRequest){
        val tripVote = tripVoteRepository.findOneById(tripPointRequest.tripId)
        if(tripPointRequest.isDriver){
            tripVote.voteGivenByDriver = tripPointRequest.point
            tripVoteRepository.save(tripVote)
            val hitchHiker = tripVote.tripRequestId!!.personRequested!!.username
            userService.reviewUser(hitchHiker!!,tripPointRequest.point,tripVote.tripRequestId?.trip?.distance ?: 0.0)
        }
        else{
            tripVote.voteGivenByHitchhiker = tripPointRequest.point
            tripVoteRepository.save(tripVote)
            val driver = tripVote.tripRequestId!!.trip!!.driverName!!.username
            userService.reviewUser(driver!!, tripPointRequest.point,tripVote.tripRequestId?.trip?.distance ?: 0.0)
        }


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

