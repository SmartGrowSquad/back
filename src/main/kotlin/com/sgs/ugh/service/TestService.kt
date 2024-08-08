package com.sgs.ugh.service

import com.sgs.ugh.controller.response.GetTestResponse
import org.springframework.stereotype.Service

@Service
class TestService {
    fun testGet(req: String): GetTestResponse {
        return GetTestResponse(req)
    }
}