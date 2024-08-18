package com.sgs.ugh.controller

import com.sgs.ugh.AbstractIntegrationAppTest
import com.sgs.ugh.controller.request.SigninRequest
import com.sgs.ugh.entity.Member
import com.sgs.ugh.repository.MemberRepository
import io.kotest.assertions.print.print
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

class MemberControllerTest(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
): AbstractIntegrationAppTest() {
    private val log = LoggerFactory.getLogger(MemberControllerTest::class.java)
    init {
        beforeSpec {
            memberRepository.save(Member("member1", "member1@google.com", passwordEncoder.encode("1234"), "서울특별시 도림로 22길 8 103-1401", null, "ROLE_CUSTOMER"))
            memberRepository.save(Member("member2", "member2@google.com", passwordEncoder.encode("4567"), "서울특별시 도림로 22길 8 102-1401", null, "ROLE_CUSTOMER"))
            memberRepository.save(Member("member3", "member3@google.com", passwordEncoder.encode("7890"), "서울특별시 도림로 22길 8 104-1401", null, "ROLE_CUSTOMER"))
            memberRepository.save(Member("member4", "member4@google.com", passwordEncoder.encode("2345"), "서울특별시 도림로 22길 8 101-1401", null, "ROLE_CUSTOMER"))
        }

        describe("유저 컨트롤러 테스트") {
            describe("user get test") {
                val memberId: Long = 1L

                context("인증 받은 사용자") {
                    val request = SigninRequest("member1@google.com", "1234")
                    val jsonRequest = mapper.writeValueAsString(request)

                    val response = mockMvc.post("/v1/auth/sign-in") {
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        content = jsonRequest
                    }.andReturn()

                    it("사용자 조회 가능") {
                        mockMvc.get("/v1/member/$memberId") {
                            header("Authorization", "Bearer " + response.response.getHeaderValue("Authorization"))
                            header("rft", "Bearer " + response.response.getHeaderValue("rft")!!)
                        }.andExpect { status { isOk() } }
                            .andDo { print() }
                    }
                }
                context("인증 받지 않은 사용자가 getUser") {
                    it("예외처리") {
                        mockMvc.get("/v1/member/${memberId}")
                            .andExpect { status { isForbidden() }}
                            .andDo { print() }
                    }
                }
            }
        }
    }
}