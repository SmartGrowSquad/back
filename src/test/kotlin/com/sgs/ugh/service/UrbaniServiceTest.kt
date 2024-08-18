package com.sgs.ugh.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.sgs.ugh.controller.request.CreateUrbaniRequest
import com.sgs.ugh.controller.response.CreateMemberResponse
import com.sgs.ugh.controller.response.CreateUrbaniResponse
import com.sgs.ugh.entity.Urbani
import com.sgs.ugh.exception.AlreadyExistException
import com.sgs.ugh.repository.UrbaniRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalTime

class UrbaniServiceTest: DescribeSpec({

    val objectMapper = ObjectMapper()
    val urbaniRepository: UrbaniRepository = mockk<UrbaniRepository>()
    val urbaniService: UrbaniService = UrbaniService(urbaniRepository, objectMapper)

    describe("urbani service test") {
        val dayOfWeek = arrayOf("0", "1", "1", "1", "1", "1", "1")
        val openTime = LocalTime.of(9, 0)
        val closeTime = LocalTime.of(20, 30)

        describe("create urbani") {

            val req = CreateUrbaniRequest(
                "어반이1",
                "서울특별시 도림로 22길 8 103-1401",
                dayOfWeek,
                openTime,
                closeTime
            )
            every { urbaniRepository.save(any()) } returns Urbani(
                "어반이1",
                "서울특별시 도림로 22길 8 103-1401",
                null,
                objectMapper.writeValueAsString(dayOfWeek),
                openTime,
                closeTime
            )
            describe("새로운 어반이") {
                every { urbaniRepository.findByName( any() ) } returns null
                it("생성된 어반이 반환") {
                    val result = urbaniService.createUrbani(req)

                    result::class shouldBe CreateUrbaniResponse::class
                    result.availableCrop shouldBe mutableSetOf()
                    result.name shouldBe "어반이1"
                }
            }
            describe("존재하는 어반이") {
                every { urbaniRepository.findByName( any() ) } throws AlreadyExistException()
                it("AlreadyExistException 반환") {
                    val exception = shouldThrow<AlreadyExistException> {
                        urbaniService.createUrbani(req)
                    }

                    exception.message shouldBe "Already Exist"

                }

            }
        }
        xdescribe("delete urbani") {

        }
        describe("search urbani") {
            val uran1 = Urbani("어반이1", "서울특별시 도림로 22길 8 103-1401", null, objectMapper.writeValueAsString(dayOfWeek), openTime, closeTime)
            val uran2 = Urbani("어반이2", "서울특별시 도림로 22길 8 103-1402", null, objectMapper.writeValueAsString(dayOfWeek), openTime, closeTime)

            describe("근처에 어반이가 존재함") {
                every { urbaniRepository.findAllByLocation( any() ) } returns mutableSetOf(uran1, uran2)
                it("어반이 set 반환") {
                    val res = urbaniService.searchNearUrbani("서울특별시 도림로")

                    res.result.size shouldNotBe 0

                }
            }
            describe("근처에 어반이가 존재하지 않음") {
                every { urbaniRepository.findAllByLocation( any() ) } returns mutableSetOf()
                it("빈 set 반환") {
                    val res = urbaniService.searchNearUrbani("서울특별시 도림로")

                    res.result.size shouldBe 0
                }
            }
        }
    }
})