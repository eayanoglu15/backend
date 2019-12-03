package com.example.fourtytwo.modules.users.request

import org.hibernate.annotations.NaturalId
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class NewUserRequest(
        var email: String,
        var password: String,
        var firstName: String?=null,
        var surname: String?=null,
        var username: String,
        var phone: String? =null,
        var age: Int? =null,
        var sex: String?="Not_Specified",
        var isDriver: Boolean=false,
        var plaque: String?=null,
        var carModel: String?=null
)