package com.sgs.ugh.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * 타임존에 의한 시간 유틸리티
 */
object EpochTimeUtil {
    private const val TIME_ZONE = "Asia/Seoul"

    fun getCurrentEpochTime() = ZonedDateTime.now(ZoneId.of(TIME_ZONE))
        .toInstant()
        .toEpochMilli()

    fun convertEpochTimeToLocalDateTime(epochTime: Long): LocalDateTime = ZonedDateTime.ofInstant(
        java.time.Instant.ofEpochMilli(epochTime),
        ZoneId.of(TIME_ZONE)
    ).toLocalDateTime()
}