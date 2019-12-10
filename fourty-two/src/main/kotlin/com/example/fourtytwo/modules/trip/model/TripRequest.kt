package com.example.fourtytwo.modules.trip.model

import com.example.fourtytwo.modules.users.model.User
import org.hibernate.annotations.NaturalId
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
        name = "trip_request"
)
class TripRequest{
    @field:OneToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "trip_id",referencedColumnName ="id" )
    var trip: Trip?=null

    @field:OneToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "person_requested",referencedColumnName ="username" )
    var personRequested:User?=null

    var isAccepted:Boolean=false

    var showable:Boolean=true

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    var id: Long? = null

    var version: LocalDateTime? = LocalDateTime.now(ZoneId.of("UTC"))
}
