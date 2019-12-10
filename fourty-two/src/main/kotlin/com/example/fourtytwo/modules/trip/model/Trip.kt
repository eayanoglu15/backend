package com.example.fourtytwo.modules.trip.model

import com.example.fourtytwo.modules.users.model.User
import org.hibernate.annotations.NaturalId
import java.time.Instant
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
import javax.persistence.UniqueConstraint
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(
        name = "trip" )
class Trip{
    @field:OneToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "driver_name", referencedColumnName = "username")
    var driverName: User? = null

    var fromWhere:String?=null

    var toWhere:String?=null

    var startTime: Instant?=null

    var endTime:Instant?=null

    var totalSeatNumber:Int=0

    var availableSeatNumber:Int=0

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    var id: Long? = null

    var version: LocalDateTime? = LocalDateTime.now(ZoneId.of("UTC"))
}

