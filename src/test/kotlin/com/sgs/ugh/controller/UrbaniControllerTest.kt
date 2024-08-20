package com.sgs.ugh.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.sgs.ugh.AbstractIntegrationAppTest
import com.sgs.ugh.controller.request.CreateUrbaniRequest
import com.sgs.ugh.controller.request.SigninRequest
import com.sgs.ugh.entity.Member
import com.sgs.ugh.repository.MemberRepository
import com.sgs.ugh.repository.UrbaniRepository
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.post
import java.time.LocalTime

class UrbaniControllerTest(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val objectMapper: ObjectMapper
): AbstractIntegrationAppTest() {
    private val log = LoggerFactory.getLogger(UrbaniControllerTest::class.java)
    init {

        beforeSpec {
            memberRepository.save(Member("member1", "member1@google.com", passwordEncoder.encode("1234"), "서울특별시 도림로 22길 8 103-1401", null, "ROLE_ADMIN"))
            memberRepository.save(Member("member2", "member2@google.com", passwordEncoder.encode("1234"), "서울특별시 도림로 22길 8 103-1402", null, "ROLE_CUSTOMER"))
        }

        describe("어반이 컨트롤러 테스트") {
            describe("어반이 생성 테스트") {
                context("어반이 생성은 관리자만 가능") {
                    val request = SigninRequest("member1@google.com", "1234")
                    val jsonRequest = objectMapper.writeValueAsString(request)

                    val response = mockMvc.post("/v1/auth/sign-in") {
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        content = jsonRequest
                    }.andReturn()

                    it("어반이 생성") {
                        val dayOfWeek = arrayOf("0", "1", "1", "1", "1", "1", "1")
                        val openTime = LocalTime.of(9, 0)
                        val closeTime = LocalTime.of(20, 30)

                        val req = CreateUrbaniRequest("어반이1", "서울특별시 도림로 22길 8 103-1401",  dayOfWeek, openTime, closeTime)
                        val test = objectMapper.writeValueAsString(req)
                        mockMvc.post("/v1/urbani/create-urbani") {
                            contentType = MediaType.APPLICATION_JSON
                            accept = MediaType.APPLICATION_JSON
                            content = test
                            header("Authorization", "Bearer " + response.response.getHeaderValue("Authorization"))
                            header("rft", "Bearer " + response.response.getHeaderValue("rft"))
                        }.andExpect { status { isCreated() } }
                            .andDo { print() }
                    }
                    it("어반이 중복 생성 불가능") {
                        val dayOfWeek = arrayOf("0", "1", "1", "1", "1", "1", "1")
                        val openTime = LocalTime.of(9, 0)
                        val closeTime = LocalTime.of(20, 30)

                        val req = CreateUrbaniRequest("어반이1", "서울특별시 도림로 22길 8 103-1401",  dayOfWeek, openTime, closeTime)
                        val test = objectMapper.writeValueAsString(req)

                        mockMvc.post("/v1/urbani/create-urbani") {
                            contentType = MediaType.APPLICATION_JSON
                            accept = MediaType.APPLICATION_JSON
                            content = test
                            header("Authorization", "Bearer " + response.response.getHeaderValue("Authorization"))
                            header("rft", "Bearer " + response.response.getHeaderValue("rft"))
                        }.andExpect { status { isConflict() } }
                            .andDo { print() }
                    }
                }
                context("관리자가 아닐 경우 생성하지 못함") {
                    val request = SigninRequest("member2@google.com", "1234")
                    val jsonRequest = objectMapper.writeValueAsString(request)

                    val response = mockMvc.post("/v1/auth/sign-in") {
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        content = jsonRequest
                    }.andReturn()

                    it("403 반환") {
                        val dayOfWeek = arrayOf("0", "1", "1", "1", "1", "1", "1")
                        val openTime = LocalTime.of(9, 0)
                        val closeTime = LocalTime.of(20, 30)

                        val req = CreateUrbaniRequest("어반이2", "서울특별시 도림로 22길 8 103-1401",  dayOfWeek, openTime, closeTime)
                        val test = objectMapper.writeValueAsString(req)
                        mockMvc.post("/v1/urbani/create-urbani") {
                            contentType = MediaType.APPLICATION_JSON
                            accept = MediaType.APPLICATION_JSON
                            content = test
                            header("Authorization", "Bearer " + response.response.getHeaderValue("Authorization"))
                            header("rft", "Bearer " + response.response.getHeaderValue("rft"))
                        }.andExpect { status { isForbidden() } }
                            .andDo { print() }
                    }
                }
            }
            describe("어반이 조회") {}
        }
    }
}