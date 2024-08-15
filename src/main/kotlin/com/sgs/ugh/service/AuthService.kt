package com.sgs.ugh.service

import com.sgs.ugh.controller.request.SigninRequest
import com.sgs.ugh.controller.response.SigninResponse
import com.sgs.ugh.dto.CustomerDto
import com.sgs.ugh.repository.MemberRepository
import com.sgs.ugh.utils.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val encoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {
    fun signin(req: SigninRequest): SigninResponse {
        val user = memberRepository.findUserByEmail(req.email) ?: throw RuntimeException("not found")

        encoder.matches(user.password, req.password) ?: throw RuntimeException()

        val userDto = CustomerDto(user.id!!, user.name, user.email, user.password)
        val accessToken = jwtUtil.createAccessToken(userDto)
        val refreshToken = jwtUtil.createRefreshToken(userDto)

        return SigninResponse(accessToken, refreshToken)
    }
}