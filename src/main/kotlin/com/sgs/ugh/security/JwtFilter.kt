package com.sgs.ugh.security

import com.sgs.ugh.controller.AuthController
import com.sgs.ugh.utils.JwtUtil
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter


class JwtFilter(
    private val customUserDetailsService: CustomUserDetailsService,
    private val jwtUtil: JwtUtil
): OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(JwtFilter::class.java)
    /**
     * JWT 토큰 검증 필터
     *
     * act 가 유효함
     *  인증 진행
     * act 가 유효하지 않음
     *  rft 확인
     *      rft 가 유효함
     *          act 발급
     *          인증 진행
     *      rft 가 유효하지 않음
     *          재인증 요청
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val authorizationHeader = request.getHeader("Authorization")
        val refreshToken = request.getHeader("rft")
        // JWT is present in the header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val token = authorizationHeader.substring(7)
            // Validate the JWT token
            try {
                if(jwtUtil.validateToken(token)) {
                    val userEmail = jwtUtil.getUserEmail(token)
                    log.info(userEmail)
                    // Create userDetails if the user and token match
                    val userDetails = customUserDetailsService.loadUserByUsername(userEmail!!)

                    if (userDetails != null) {

                        // Create an authentication token with UserDetails, Password, and Role
                        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.authorities
                        )
                        // Set the authentication in the current request's Security Context
                        SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
                    }
                }
            } catch (e: ExpiredJwtException) {
                // validate rft
                if(jwtUtil.validateToken(refreshToken)) {
                    // Reissue act
                    val newACT = jwtUtil.reIssueAccessToken(token)
                    val newRFT = jwtUtil

                    val userEmail = jwtUtil.getUserEmail(newACT)

                    // Create userDetails if the user and token match
                    val userDetails = customUserDetailsService.loadUserByUsername(userEmail.toString())

                    if (userDetails != null) {

                        // Create an authentication token with UserDetails, Password, and Role
                        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.authorities
                        )

                        response.setHeader("Authorization", newACT)
                        // Set the authentication in the current request's Security Context
                        SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
                    }

                }
            } catch(e: Exception) {
                log.warn(e.message)
                log.warn("filter exception")
            }
        }

        filterChain.doFilter(request, response) // Pass the request to the next filter
    }
}