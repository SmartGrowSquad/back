package com.sgs.ugh.service

import com.sgs.ugh.controller.request.SigninRequest
import com.sgs.ugh.controller.response.SigninResponse
import com.sgs.ugh.dto.CustomerDto
import com.sgs.ugh.exception.MemberNotFoundException
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
        val member = memberRepository.findMemberByEmail(req.email) ?: throw MemberNotFoundException()

        encoder.matches(req.password, member.password) ?: throw RuntimeException()

        val userDto = CustomerDto(member.id!!, member.name, member.password, member.email, member.role)

        val accessToken = jwtUtil.createAccessToken(userDto)
        val refreshToken = jwtUtil.createRefreshToken(userDto)

        return SigninResponse(accessToken, refreshToken)
    }
}