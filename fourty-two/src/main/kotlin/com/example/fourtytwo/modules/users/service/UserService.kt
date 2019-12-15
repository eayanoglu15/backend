package com.example.fourtytwo.modules.users.service

import com.example.fourtytwo.modules.shared.ApiResponse
import com.example.fourtytwo.modules.shared.RestException
import com.example.fourtytwo.modules.shared.asOkResponse
import com.example.fourtytwo.modules.users.model.User
import com.example.fourtytwo.modules.users.model.UserRepository
import com.example.fourtytwo.modules.users.request.LoginRequest
import com.example.fourtytwo.modules.users.request.NewUserRequest
import com.example.fourtytwo.modules.users.request.ReviewUser
import com.example.fourtytwo.modules.users.response.ReviewResponse
import net.bytebuddy.implementation.bytecode.Throw
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService @Autowired constructor(
        private val userRepository: UserRepository

) {
    fun getUserByUsername(username: String): User {
        // Get the user for the id
        val userEntity = userRepository.findByUsername(username)
        return userEntity!!.orElseThrow {
            RestException(
                    "Exception.notFound",
                    HttpStatus.UNAUTHORIZED,
                    "User",
                    username
            )
        }
    }

    fun getUserById(userId: Long): User {
        // Get the user for the id
        val userEntity = userRepository.findById(userId)
        return userEntity.orElseThrow {
            RestException(
                    "Exception.notFound",
                    HttpStatus.UNAUTHORIZED,
                    "User",
                    userId
            )
        }
    }

    fun existsByUsername(username: String): Boolean = userRepository.existsByUsername(username)



 /*   @Transactional
    fun updateUser(user: User): User {
        return userRepository.save(user)
    }*/


    @Transactional
    fun updateUser(user: User): User {
        if(!existsByUsername(user.username!!)){
            throw RestException(
                    "Exception.notFound",
                    HttpStatus.UNAUTHORIZED,
                    "User",
                    user.username!!
            )
        }
        else{
            var oldUser = getUserByUsername(user.username!!)

                oldUser.username = user.username
                oldUser.firstName = user.firstName
                oldUser.surname = user.surname
                oldUser.phone = user.phone
                oldUser.email = user.email
                oldUser.password = user.password
                oldUser.age = user.age
                oldUser.sex = user.sex
            if(oldUser.driver!!){
                oldUser.carModel = user.carModel
                oldUser.plaque = user.plaque
            }

            return userRepository.save(user)
        }
    }


    @Transactional
    fun reviewUser(username: String, point: Int) {
        if(!existsByUsername(username)){
            throw RestException(
                    "Exception.notFound",
                    HttpStatus.UNAUTHORIZED,
                    "User",
                    username
            )
        }
        else{
            var user = getUserByUsername(username)
            user.point=(user.point*user.numberRevieved+point)/(user.numberRevieved+1)
            user.numberRevieved=user.numberRevieved+1
            userRepository.save(user)
        }
    }

    @Transactional
 /*   fun reviewUser1(review: ReviewUser): ReviewResponse {
        if(!existsByUsername(review.giverUsername) || !existsByUsername(review.receiverUsername)) {
            throw RestException(
                    "Exception.notFound",
                    HttpStatus.UNAUTHORIZED,
                    "User",
                    review.giverUsername
            )
        }else{
            var user = getUserByUsername(review.receiverUsername)
            user.point=(user.point*user.numberRevieved+review.review)/(user.numberRevieved+1)
            var point=user.point
            user.numberRevieved=user.numberRevieved+1
            userRepository.save(user)
            return ReviewResponse(
                    updatedReviewPoint = point
            )
        }


    }*/

    fun login(login: LoginRequest):Boolean {
        var user=getUserByUsername(login.username)
        if(!user.password.equals(login.password)){
            return false
        }
        return true
    }


}

