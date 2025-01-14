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
import org.springframework.test.web.servlet.post

@Sql(scripts = ["classpath:testdata.sql"])
class AuthControllerTest: AbstractIntegrationAppTest() {
    private val log = LoggerFactory.getLogger(AuthControllerTest::class.java)
    val memberRepository = mockk<MemberRepository>()
    val encoder = mockk<PasswordEncoder>()
    init {

        describe("Auth controller test") {
            describe("등록된 사용자가 로그인 하는 경우 act 와 rct 를 받는다.") {
                context("등록된 사용자가 로그인 요청") {
                    val request = SigninRequest("test@email.com", "password")
                    val jsonRequest = mapper.writeValueAsString(request)
                    it("test") {
                        every { encoder.matches(any(), any()) } returns true
                        every { memberRepository.findMemberByEmail( any() ) } returns Member("username", request.email, "passcode", "address", null, Role.ROLE_CUSTOMER.name)

                        mockMvc.post("/v1/auth/sign-in") {
                            contentType = MediaType.APPLICATION_JSON
                            accept = MediaType.APPLICATION_JSON
                            content = jsonRequest
                        } .andExpect { status { isOk() } }
                    }
                }
            }
        }
    }
}