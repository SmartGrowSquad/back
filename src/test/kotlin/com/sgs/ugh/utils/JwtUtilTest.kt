package com.sgs.ugh.utils

import com.sgs.ugh.dto.CustomerDto
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import javax.crypto.SecretKey
class JwtUtilTest: DescribeSpec({
    val secretKey = System.getenv("JWT_SECRET_KEY")
    val key: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
    val jwtUtil = JwtUtil(secretKey, 100000, 100000)
    val userDto = CustomerDto(1, "name",  "encoded password", "test@email.com", Role.ROLE_CUSTOMER.name)

    describe("JWT Util Test") {

        describe("createAccessToken method") {

            context("토큰 생성") {
                // TODO 테스트 실패 수정
                it("방금 생성된 토큰은 유효하다") {
                    val act = jwtUtil.createAccessToken(userDto)
                    val userEmail = jwtUtil.getUserEmail(act)

                    userEmail shouldBe userDto.email
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
        xdescribe("t") {
            context("RFT 재발급") {

            }
        }

    }
})