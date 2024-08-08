package com.sgs.ugh.service

import com.sgs.ugh.controller.response.GetTestResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class TestServiceTest: BehaviorSpec({
    lateinit var testService: TestService
    beforeTest {
        testService = TestService()
    }
    given("test get 요청") {
        `when`("valid request") {
            val request = "test"
            val response: GetTestResponse = testService.testGet(request)

            then("return GetTestResponse") {
                response.response shouldBe request
            }
        }

    }
})