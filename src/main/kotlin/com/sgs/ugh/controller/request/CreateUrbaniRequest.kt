package com.sgs.ugh.controller.request

import java.time.LocalDateTime
import java.time.LocalTime

data class CreateUrbaniRequest(
    val name: String,
    val location: String,
    val dayOfWeek: Array<String>,
    val openTime: LocalTime,
    val closeTime: LocalTime
)
