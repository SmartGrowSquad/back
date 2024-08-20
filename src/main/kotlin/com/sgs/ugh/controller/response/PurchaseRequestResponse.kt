package com.sgs.ugh.controller.response

import java.time.LocalDateTime

data class PurchaseRequestResponse (
    val id: Long,
    val name: String,
    val passcode: String,
    val createdAt: LocalDateTime
)