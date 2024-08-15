package com.sgs.ugh.utils

import com.sgs.ugh.dto.CustomerDto
import com.sgs.ugh.dto.UserDto
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import javax.crypto.SecretKey

class JwtUtilTest: DescribeSpec({
    val secretKey = "7e8439e771be369dcb8b25ef00a2ea8782511c68260671ac75fcf8459a068618cd2eedf9880b44a2a677cde5f5f85419bf5b264aecf084e5017b2b86451e6e1c"
    val key: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
    val jwtUtil = JwtUtil(secretKey, 100000, 100000)
    val userDto = CustomerDto(1, "name", "test@email.com", "encoded password")

    describe("JWT Util Test") {

        describe("createAccessToken method") {

            context("토큰 생성") {

                it("방금 생성된 토큰은 유효하다") {
                    val act = jwtUtil.createAccessToken(userDto)
                    val jwt = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(act).payload

                    jwt shouldBe userDto.email
               }
            }
        }

        describe("parseClaims method") {

            context("클레림 추출") {

                it("클레임 추출 시 클레임이 추출되어야 한다.") {
                    val act = jwtUtil.createAccessToken(userDto)
                    val jwt = jwtUtil.parseClaims(act)

                    jwt.issuer shouldBe userDto.email
                }
            }
        }
        describe("") {
            context("RFT 재발급") {

            }
        }

    }
})