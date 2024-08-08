
package com.sgs.ugh.controller

import com.sgs.ugh.controller.response.GetTestResponse
import com.sgs.ugh.exception.BadRequestException
import com.sgs.ugh.service.TestService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 * @TODO 해당 테스트 코드의 적합성 검증이 필요함
 */
class TestControllerTest: DescribeSpec({
    val testService: TestService = mockk<TestService>()
    val testController = TestController(testService)

    describe("GET /test/get-test") {
        context("valid Request") {
            val request = "test request"

            it("OK") {
                every { testService.testGet( any() ) } returns GetTestResponse(request)

                val response = testController.getTest(request)
                response.statusCode shouldBe HttpStatus.OK
            }
        }

        xcontext("invalid request") {
            val request = ""

            it("bad request") {
                every { testService.testGet( any() ) } returns GetTestResponse(request)

                val response = testController.getTest("")
                response.statusCode shouldBe HttpStatus.BAD_REQUEST
            }
        }
    }
})