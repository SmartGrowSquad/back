package com.sgs.ugh.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.sgs.ugh.AbstractIntegrationAppTest
import com.sgs.ugh.controller.request.PurchaseRequest
import com.sgs.ugh.controller.request.SigninRequest
import com.sgs.ugh.entity.AvailableCrop
import com.sgs.ugh.entity.Member
import com.sgs.ugh.entity.Urbani
import com.sgs.ugh.repository.AvailableCropRepository
import com.sgs.ugh.repository.MemberRepository
import com.sgs.ugh.repository.UrbaniRepository
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.post
import java.time.LocalTime

class PurchaseControllerTest(
    private val memberRepository: MemberRepository,
    private val urbaniRepository: UrbaniRepository,
    private val availableCropRepository: AvailableCropRepository,
    private val passwordEncoder: PasswordEncoder,
    private val objectMapper: ObjectMapper
): AbstractIntegrationAppTest() {
    init {
        beforeSpec {
            memberRepository.save(Member("member1", "member1@google.com", passwordEncoder.encode("1234"), "서울특별시 도림로 22길 8 103-1401", null, "ROLE_ADMIN"))
            memberRepository.save(Member("member2", "member2@google.com", passwordEncoder.encode("1234"), "서울특별시 도림로 22길 8 103-1402", null, "ROLE_CUSTOMER"))
            val urbani = urbaniRepository.save(Urbani("어반이1", "서울특별시 도림로 22길 8 103-1401", null, objectMapper.writeValueAsString(arrayOf("0", "1", "1", "1", "1", "1", "1")), LocalTime.of(9, 0), LocalTime.of(20, 30)))
            availableCropRepository.save(AvailableCrop("방울토마토", 5600.00, "LED 빔 맞고 자란 토마토", 10, false, urbani))
            availableCropRepository.save(AvailableCrop("상추", 5600.00, "농약 상추 먹으면 죽음", 1, false, urbani))
        }

        describe("purchase controller test") {
            describe("구매 요청") {
                val request = SigninRequest("member1@google.com", "1234")
                val jsonRequest = objectMapper.writeValueAsString(request)

                val response = mockMvc.post("/v1/auth/sign-in") {
                    contentType = MediaType.APPLICATION_JSON
                    accept = MediaType.APPLICATION_JSON
                    content = jsonRequest
                }.andReturn()
                val req = PurchaseRequest(1L, 2, 1L)
                val jsonReq = objectMapper.writeValueAsString(req)
                it("구매") {
                    mockMvc.post("/v1/purchase/") {
                        contentType = MediaType.APPLICATION_JSON
                        accept = MediaType.APPLICATION_JSON
                        content = jsonReq
                        header("Authorization", "Bearer " + response.response.getHeaderValue("Authorization"))
                        header("rft", "Bearer " + response.response.getHeaderValue("rft"))
                    }.andExpect { status { isCreated() } }
                        .andDo { print() }

                }
            }
            describe("구매 승인") {}
            describe("구매 취소") {}
            describe("구매 내역 조회") {}
        }
    }
}