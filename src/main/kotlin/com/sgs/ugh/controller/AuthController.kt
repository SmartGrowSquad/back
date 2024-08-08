package com.sgs.ugh.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/auth")
class AuthController(

) {
    @PostMapping("/sign-in")
    fun signin() {}

    @PostMapping("/sign-out")
    fun signout() {}
}