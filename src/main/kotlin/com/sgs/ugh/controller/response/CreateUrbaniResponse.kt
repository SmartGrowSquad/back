package com.sgs.ugh.controller.response

import com.sgs.ugh.entity.AvailableCrop
import java.time.LocalTime

data class CreateUrbaniResponse(
    val name: String,
    val location: String,
    val cLocation: String?,
    val dayOfWeek: Array<String>,
    val openTime: LocalTime,
    val closeTime: LocalTime,
    val availableCrop: MutableSet<AvailableCrop>
)