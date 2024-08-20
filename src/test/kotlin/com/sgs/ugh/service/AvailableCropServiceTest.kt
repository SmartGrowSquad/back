package com.sgs.ugh.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.sgs.ugh.controller.request.SaveAvailableCropRequest
import com.sgs.ugh.controller.request.UpdateCropAmountRequest
import com.sgs.ugh.entity.AvailableCrop
import com.sgs.ugh.entity.Urbani
import com.sgs.ugh.repository.AvailableCropRepository
import com.sgs.ugh.repository.UrbaniRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.platform.commons.logging.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalTime

class AvailableCropServiceTest: DescribeSpec({
    val availableCropRepository: AvailableCropRepository = mockk<AvailableCropRepository>()
    val urbaniRepository: UrbaniRepository = mockk<UrbaniRepository>()
    val availableCropService = AvailableCropService(availableCropRepository, urbaniRepository)
    val objectMapper = ObjectMapper()

    val log = LoggerFactory.getLogger(AvailableCropServiceTest::class.java)

    describe("available crop service test") {
        val dayOfWeek = arrayOf("0", "1", "1", "1", "1", "1", "1")
        val openTime = LocalTime.of(9, 0)
        val closeTime = LocalTime.of(20, 30)
        val urbani = Urbani(
            "어반이1",
            "서울특별시 도림로 22길 8 103-1401",
            null,
            objectMapper.writeValueAsString(dayOfWeek),
            openTime,
            closeTime
        )
        val crop = AvailableCrop("방울토마토", 5600.00, "LED 빔 맞고 자란 토마토", 10, false, urbani).apply { this.id = 1L }
        describe("save crop") {
            describe("어반이가 가지고 있는 crop의 이름은 중복이 없다.") {

                val req = SaveAvailableCropRequest("방울토마토", 5600.00, "LED 빔 맞고 자란 토마토", 10, 1L)
                it("중복되지 않은 이름의 crop 은 저장") {
                    // todo 중복되는 테스트 코드 정리하기
                    every { availableCropRepository.findByNameAndUrbaniId(any(), any()) } returns null
                    every { urbaniRepository.findByIdOrNull(any()) } returns urbani
                    every { availableCropRepository.save( any() ) } returns crop
                    val res = availableCropService.saveCrop(req)

                    res.status shouldBe false
                    res.name shouldBe req.name
                }
            }

        }
        describe("update crop amount") {
            describe("crop의 수량을 수정한다. 감소") {
                every { availableCropRepository.findByIdOrNull( any() ) } returns crop

                it("구매로 인한 수량 감소") {
                    val req = UpdateCropAmountRequest(1L, 2, false)
                    val res = availableCropService.updateCropAmount(req)

                    res.amount shouldBe 8
                }

            }
            describe("crop의 수량을 수정한다. 증가") {
                every { availableCropRepository.findByIdOrNull( any() ) } returns crop
                it("수확으로 인한 수량 증가") {
                    val req = UpdateCropAmountRequest(1L, 2, true)
                    val res = availableCropService.updateCropAmount(req)

                    res.amount shouldBe 10
                }
            }

        }
    }
})