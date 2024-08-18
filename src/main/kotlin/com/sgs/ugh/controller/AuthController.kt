package com.sgs.ugh.controller

import com.sgs.ugh.controller.request.SigninRequest
import com.sgs.ugh.controller.response.SigninResponse
import com.sgs.ugh.service.AuthService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 인증 컨트롤러
 *
 */
@Tag(name = "Auth Api")
@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    private val log = LoggerFactory.getLogger(AuthController::class.java)
    @PostMapping("/sign-in")
    fun signin(
        @Valid @RequestBody req: SigninRequest,
        response: HttpServletResponse
    ): ResponseEntity<String> {
        val tokens = authService.signin(req)

        response.setHeader("Authorization", tokens.accessToken)
        response.setHeader("rft", tokens.refreshToke)

        log.info("login request come ${req.email}")

        return ResponseEntity.ok().body("accessed!")
    }

    @PostMapping("/sign-out")
    fun signout() {}
}