package com.example.fourtytwo.modules.trip.model

import com.example.fourtytwo.modules.users.model.User
import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.NotBlank

@Entity
@Table(
        name = "trip_vote"
)
class TripVote{
    @field:OneToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "trip_request_id",referencedColumnName = "id")
    var tripRequestId: TripRequest?=null

    var voteGiven: Int?=null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    var id: Long? = null

    var version: LocalDateTime? = LocalDateTime.now(ZoneId.of("UTC"))
}