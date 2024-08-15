package com.sgs.ugh.entity.baseEntity

import com.sgs.ugh.utils.EpochTimeUtil
import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PreUpdate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@MappedSuperclass
open abstract class MutableEntity: ImmutableEntity() {
    @LastModifiedDate
    @Column(
        name = "updated_at",
        nullable = true,
        updatable = true
    )
    var updatedAt: LocalDateTime? = null
    @PreUpdate
    fun onUpdate() {
        val createdTime = EpochTimeUtil.getCurrentEpochTime()
        updatedAt =  EpochTimeUtil.convertEpochTimeToLocalDateTime(createdTime)
    }
}