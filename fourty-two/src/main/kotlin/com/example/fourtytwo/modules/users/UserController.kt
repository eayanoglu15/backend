package com.example.fourtytwo.modules.users


import com.example.fourtytwo.modules.shared.ApiResponse
import com.example.fourtytwo.modules.shared.RestException
import com.example.fourtytwo.modules.shared.asOkResponse
import com.example.fourtytwo.modules.users.model.User
import com.example.fourtytwo.modules.users.model.UserRepository
import com.example.fourtytwo.modules.users.request.LoginRequest
import com.example.fourtytwo.modules.users.request.NewUserRequest
import com.example.fourtytwo.modules.users.request.ReviewUser
import com.example.fourtytwo.modules.users.response.ReviewResponse
import com.example.fourtytwo.modules.users.service.UserService
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/users")
class UserRestController(
        val userRepository: UserRepository,
        val userService: UserService,
        val messageSource: MessageSource
) {
    @GetMapping(path = [("/{username}")])
    fun getUser(@PathVariable("username") username: String): User {
        return userService.getUserByUsername(username)
    }

    @PutMapping("newUser")
    fun newUser(@RequestBody user: User,
                locale: Locale): ResponseEntity<ApiResponse> {
        if (user.username != null && !userService.existsByUsername(user.username!!)) {
            userRepository.save(user)
            return ApiResponse.fromMessage(messageSource, locale, true, "General.successfulSave").asOkResponse()
        } else {
            return ApiResponse.fromMessage(messageSource, locale, false, "Auth.usernameInUse").asOkResponse()
        }
    }

    @PostMapping("updateUser")
    fun updateUser(@RequestBody user: User,
                locale: Locale): ResponseEntity<User> {
            return userService.updateUser(user).asOkResponse()
    }

/*    @PostMapping("updateUser")
    @ResponseStatus(HttpStatus.OK)
    fun updateUser(user: User) {
        userRepository.save(user)
    }*/

   /* @PostMapping("/review")
    fun reviewUser(@RequestBody review: ReviewUser,
                   locale: Locale): ResponseEntity<ReviewResponse> {
       return userService.reviewUser(review).asOkResponse()
    }*/

    @PostMapping("/login")
    fun loginUser(@RequestBody login: LoginRequest,
                  locale: Locale): ResponseEntity<User> {
        if(!userService.login(login)){
            throw  RestException(
                    "Exception.notFound",
                    HttpStatus.UNAUTHORIZED,
                    "User",
                    login.password
            )
        }else
        return userService.getUserByUsername(login.username).asOkResponse()
    }

}