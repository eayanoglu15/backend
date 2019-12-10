package com.example.fourtytwo.modules.trip

import com.example.fourtytwo.modules.shared.ApiResponse
import com.example.fourtytwo.modules.shared.asOkResponse
import com.example.fourtytwo.modules.trip.request.AcceptTripRequest
import com.example.fourtytwo.modules.trip.request.CreateTripRequest
import com.example.fourtytwo.modules.trip.request.DriverTripCheckRequest
import com.example.fourtytwo.modules.trip.request.SendRequestRequest
import com.example.fourtytwo.modules.trip.response.DriverRequestsPageRequest
import com.example.fourtytwo.modules.trip.response.TripResponse
import com.example.fourtytwo.modules.trip.service.TripService
import com.example.fourtytwo.modules.users.service.UserService
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("/trip")
class TripController(
        val tripService: TripService,
        val userService: UserService,
        val messageSource: MessageSource
) {
    @GetMapping("getAllTrips")
    @ResponseStatus(HttpStatus.OK)
    fun getAllTrips(): ResponseEntity<List<TripResponse>> {
        return tripService.getAllAvailableTrips().asOkResponse()
    }

    @PutMapping("createTrip")
    fun createTrip(@RequestBody createTripRequest: CreateTripRequest,
                locale: Locale): ResponseEntity<ApiResponse> {
        tripService.createTrip(createTripRequest)
        return ApiResponse.fromMessage(messageSource, locale, true, "General.successfulSave").asOkResponse()
    }

    @PostMapping("getAllRequestsByDriver")
    fun getRequestsByDriver(@RequestBody driverTripCheck: DriverTripCheckRequest,
                locale: Locale): ResponseEntity<DriverRequestsPageRequest> {
        return tripService.getDriversAllRequests(driverTripCheck).asOkResponse()
    }

    @PostMapping("acceptRequest")
    fun acceptRequest(@RequestBody acceptTripRequest: AcceptTripRequest,
                            locale: Locale): ResponseEntity<ApiResponse> {
        tripService.acceptRequest(acceptTripRequest)
        return ApiResponse.fromMessage(messageSource, locale, true, "General.successfulSave").asOkResponse()
    }

    @PostMapping("declineRequest")
    fun declineRequest(@RequestBody acceptTripRequest: AcceptTripRequest,
                      locale: Locale): ResponseEntity<ApiResponse> {
        tripService.declineRequest(acceptTripRequest)
        return ApiResponse.fromMessage(messageSource, locale, true, "General.successfulSave").asOkResponse()
    }

    @PostMapping("makeTripRequest")
    fun makeTripRequest(@RequestBody sendRequestRequest: SendRequestRequest,
                       locale: Locale): ResponseEntity<ApiResponse> {
        tripService.requestJoinTrip(sendRequestRequest)
        return ApiResponse.fromMessage(messageSource, locale, true, "General.successfulSave").asOkResponse()
    }
}