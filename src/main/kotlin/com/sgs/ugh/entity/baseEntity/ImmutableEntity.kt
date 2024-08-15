package com.sgs.ugh.entity.baseEntity

import com.sgs.ugh.utils.EpochTimeUtil
import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

/**
 * 생성 시간 entity
 */
@MappedSuperclass
open abstract class ImmutableEntity: BaseEntity() {
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null
    @PrePersist
    fun onCreate() {
        val createdTime = EpochTimeUtil.getCurrentEpochTime()
        createdAt = EpochTimeUtil.convertEpochTimeToLocalDateTime(createdTime)
    }
}