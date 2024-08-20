package com.sgs.ugh.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.sgs.ugh.AbstractIntegrationAppTest
import com.sgs.ugh.controller.request.SaveAvailableCropRequest
import com.sgs.ugh.controller.request.SigninRequest
import com.sgs.ugh.entity.Member
import com.sgs.ugh.entity.Urbani
import com.sgs.ugh.repository.MemberRepository
import com.sgs.ugh.repository.UrbaniRepository
import com.sgs.ugh.service.AvailableCropService
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.post
import java.time.LocalTime

class AvailableCropControllerTest(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val urbaniRepository: UrbaniRepository,
    private val objectMapper: ObjectMapper
): AbstractIntegrationAppTest() {
    init {
        beforeSpec {
            val dayOfWeek = objectMapper.writeValueAsString(arrayOf("0", "1", "1", "1", "1", "1", "1"))
            val openTime = LocalTime.of(9, 0)
            val closeTime = LocalTime.of(20, 30)

            memberRepository.save(Member("member1", "member1@google.com", passwordEncoder.encode("1234"), "서울특별시 도림로 22길 8 103-1401", null, "ROLE_ADMIN"))
            memberRepository.save(Member("member2", "member2@google.com", passwordEncoder.encode("1234"), "서울특별시 도림로 22길 8 103-1402", null, "ROLE_CUSTOMER"))
            urbaniRepository.save(Urbani("어반이1", "서울특별시 도림로 22길 8 103-1401", null, dayOfWeek, openTime, closeTime))
            urbaniRepository.save(Urbani("어반이2", "서울특별시 도림로 22길 8 103-1402", null, dayOfWeek, openTime, closeTime))
        }
        describe("available crop controller test") {
            describe("save crop test") {
                val request = SigninRequest("member1@google.com", "1234")
                val signinRequest = objectMapper.writeValueAsString(request)

                val response = mockMvc.post("/v1/auth/sign-in") {
                    contentType = MediaType.APPLICATION_JSON
                    accept = MediaType.APPLICATION_JSON
                    content = signinRequest
                }.andReturn()

                it("crop 생성") {
                    val req = SaveAvailableCropRequest("방울토마토", 5600.00, "LED 빔 맞고 자란 토마토", 10, 1L)
                    val jsonRequest = objectMapper.writeValueAsString(req)

                    mockMvc.post("/v1/crop/save-crop") {
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        content = jsonRequest
                        header("Authorization", "Bearer " + response.response.getHeaderValue("Authorization"))
                        header("rft", "Bearer " + response.response.getHeaderValue("rft"))
                    }.andExpect { status { isCreated() } }
                        .andDo { print() }
                }
                it("중복된 crop 생성 불가") {
                    val req = SaveAvailableCropRequest("방울토마토", 5600.00, "LED 빔 맞고 자란 토마토", 10, 1L)
                    val jsonRequest = objectMapper.writeValueAsString(req)

                    mockMvc.post("/v1/crop/save-crop") {
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        content = jsonRequest
                        header("Authorization", "Bearer " + response.response.getHeaderValue("Authorization"))
                        header("rft", "Bearer " + response.response.getHeaderValue("rft"))
                    }.andExpect { status { isConflict() } }
                        .andDo { print() }
                }
            }
        }
    }
}