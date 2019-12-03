package com.example.fourtytwo.modules.users.model
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long>{

    fun findByUsername(username:String?): Optional<User>?

    fun existsByUsername(username: String?):Boolean


}