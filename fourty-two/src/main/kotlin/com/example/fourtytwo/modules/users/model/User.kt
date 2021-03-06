package com.example.fourtytwo.modules.users.model

import org.hibernate.annotations.NaturalId
import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import java.io.Serializable

@Entity
@Table(
        name = "users", uniqueConstraints = [
    UniqueConstraint(columnNames = ["username"])
]
)
class User : Serializable {
    @field:NotBlank
    @field:Size(max = 100)
    @field:Email
    var email: String?=null

    @field:NotBlank
    var password: String?=null

    @field:NotBlank
    @field:Size(max = 40)
    var firstName: String?=null

    @field:NotBlank
    @field:Size(max = 40)
    var surname: String?=null

    // TODO: @Phone
    @field:NaturalId
    @field:NotBlank
    @field:Size(max = 40)
    var username: String?=null


    // TODO: @Phone
    @field:NotBlank
    @field:Size(max = 40)
    var phone: String? =null

    var age: Int? =null

    var point: Double = 0.0

    var numberRevieved: Int = 0

    @field:NotBlank
    @field:Size(max = 40)
    var sex: String?="Not_Specified"

    var driver: Boolean? = null


    var plaque: String?=null


    var carModel: String?=null

    var totalDistance: Double=0.0

    var image: String? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    var id: Long? = null

    var version: LocalDateTime? = LocalDateTime.now(ZoneId.of("UTC"))
}

