package com.sgs.ugh.utils

import com.sgs.ugh.dto.CustomerDto
import org.slf4j.LoggerFactory

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil(
    @Value("\${jwt.secret-key}") private var secretKey: String,
    @Value("\${jwt.expired_time}") private var expireTime: Long,
    @Value("\${jwt.refresh_time}") private var refreshTime: Long,
) {

    private val key: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
    private val log = LoggerFactory.getLogger(JwtUtil::class.java)
    fun createAccessToken(user: CustomerDto): String {
        return createToken(user, expireTime)
    }
    fun createRefreshToken(user: CustomerDto): String {
        return createToken(user, refreshTime)
    }

    /**
     * TODO 유효한 RFT 로 ACT 재발급 시 RFT 재발행 로직 추가
     */
    fun reIssueAccessToken(oldACT: String): String {

        val claims = parseClaims(oldACT)

        val now = ZonedDateTime.now()
        val tokenValidity = now.plusSeconds(expireTime)

        return Jwts.builder().apply {
            claims(claims)
            issuedAt(Date.from(now.toInstant()))
            expiration(Date.from(tokenValidity.toInstant()))
            signWith(key)
        }.compact()
    }
    /**
     * JWT 생성
     * @param user
     * @param expireTime
     * @return JWT String
     */
    private fun createToken(user: CustomerDto, expireTime: Long): String {
        val now = ZonedDateTime.now()
        val tokenValidity = now.plusSeconds(expireTime)

        return Jwts.builder().apply {
            issuer(user.email)
            issuedAt(Date.from(now.toInstant()))
            expiration(Date.from(tokenValidity.toInstant()))
            signWith(key)
        }.compact()

    }
    /**
     * Token에서 User ID 추출
     * @param token
     * @return User ID
     */
    fun getUserEmail(token: String): String? {
        return parseClaims(token).issuer
    }

    /**
     * JWT 검증
     * @param token
     * @return IsValidate
     */
    fun validateToken(token: String): Boolean {
        return try {
            log.debug("valid Token")
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token).payload
            true
        } catch (e: SecurityException) {
            log.info("Invalid JWT Token", e)
            false
        } catch (e: MalformedJwtException) {
            log.info("Invalid JWT Token", e)
            false
        } catch (e: ExpiredJwtException) {
            log.info("Expired JWT Token", e)
            throw e
            false
        } catch (e: UnsupportedJwtException) {
            log.info("Unsupported JWT Token", e)
            false
        } catch (e: IllegalArgumentException) {
            log.info("JWT claims string is empty.", e)
            false
        }
    }

    /**
     * JWT Claims 추출
     * @param accessToken
     * @return JWT Claims
     */
    fun parseClaims(accessToken: String): Claims {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(accessToken).payload
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }
}