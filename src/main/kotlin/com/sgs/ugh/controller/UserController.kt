package com.sgs.ugh.controller

import com.sgs.ugh.controller.request.CreateUserRequest
import com.sgs.ugh.controller.response.CreateUserResponse
import com.sgs.ugh.controller.response.GetUserResponse
import com.sgs.ugh.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/user")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/create-user")
    fun createUser(
        @Valid @RequestBody req: CreateUserRequest
    ): ResponseEntity<CreateUserResponse> {
        val result: CreateUserResponse = userService.saveUser(req)
        return ResponseEntity(result, HttpStatus.CREATED)
    }

    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable userId: Long
    ): ResponseEntity<GetUserResponse> {
        return ResponseEntity(userService.getUser(userId), HttpStatus.OK)
    }

    @DeleteMapping("/delete-user/{userId}")
    fun deleteUser(
        @PathVariable userId: Long
    ): ResponseEntity<String> {
        userService.deleteUser(userId)
        return ResponseEntity("user deleted", HttpStatus.OK)
    }


}