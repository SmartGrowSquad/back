package com.sgs.ugh.controller

import com.sgs.ugh.controller.response.GetTestResponse
import com.sgs.ugh.service.TestService

import jakarta.validation.constraints.NotBlank
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController("/test")
class TestController(
    private val testService: TestService
) {
    private val log = LoggerFactory.getLogger(TestController::class.java)
    @GetMapping("/get-test/{param}")
    fun getTest(
        @PathVariable @NotBlank param: String
    ): ResponseEntity<GetTestResponse> {
        val result = testService.testGet(param)
        return ResponseEntity.ok(result)
    }
}