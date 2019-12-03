package com.example.fourtytwo.modules.users.service

import com.example.fourtytwo.modules.shared.ApiResponse
import com.example.fourtytwo.modules.shared.RestException
import com.example.fourtytwo.modules.shared.asOkResponse
import com.example.fourtytwo.modules.users.model.User
import com.example.fourtytwo.modules.users.model.UserRepository
import com.example.fourtytwo.modules.users.request.NewUserRequest
import com.example.fourtytwo.modules.users.request.ReviewUser
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



    @Transactional
    fun updateUser(user: User): User {
        return userRepository.save(user)
    }

    @Transactional
    fun reviewUser(review: ReviewUser): Int {
        if(!existsByUsername(review.username)) {
            return 500
        }else{
            var user = getUserByUsername(review.username)
            user.point=(user.point+review.review)/(user.numberRevieved+1)
            user.numberRevieved=user.numberRevieved+1
            userRepository.save(user)
            return 200
        }

    }


}

