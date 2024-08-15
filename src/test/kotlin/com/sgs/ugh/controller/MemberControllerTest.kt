package com.sgs.ugh.controller

import com.sgs.ugh.AbstractIntegrationAppTest
import com.sgs.ugh.controller.request.SigninRequest
import com.sgs.ugh.entity.Member
import com.sgs.ugh.repository.MemberRepository
import com.sgs.ugh.utils.Role
import io.mockk.every
import io.mockk.mockk
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@Sql(scripts = ["classpath:testdata.sql"])
class MemberControllerTest: AbstractIntegrationAppTest() {
    private val log = LoggerFactory.getLogger(MemberControllerTest::class.java)
    val memberRepository = mockk<MemberRepository>()
    val encoder = mockk<PasswordEncoder>()
    init {
        describe("유저 컨트롤러 테스트") {
            describe("user get test") {
                val userId: Long = 1
                context("인증 받은 사용자") {
                    val request = SigninRequest("test@email.com", "password")
                    val jsonRequest = mapper.writeValueAsString(request)

                    every { encoder.matches(any(), any()) } returns true
                    every { memberRepository.findMemberById( any() ) } returns Member("username", request.email, "passcode", "address", null, Role.ROLE_CUSTOMER.name)

                    val response = mockMvc.post("/v1/auth/sign-in") {
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        content = jsonRequest
                    }.andReturn()

                    it("사용자 조회 가능") {
                        mockMvc.get("/v1/user/${userId}") {
                            header("Authorization", response.response.getHeaderValue("Authorization")!!)
                            header("RFT", response.response.getHeaderValue("RFT")!!)
                        }.andExpect { status { isOk() } }
                            .andDo { print() }

                    }
                }
                context("인증 받지 않은 사용자가 getUser") {


                    it("예외처리") {
                        mockMvc.get("/v1/user/${userId}")
                            .andExpect {  status { isForbidden() }}
                    }
                }
            }
        }
    }
}