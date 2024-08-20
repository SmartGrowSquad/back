package com.sgs.ugh.repository

import com.sgs.ugh.entity.AvailableCrop
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AvailableCropRepository: JpaRepository<AvailableCrop, Long> {
    fun findByName(name: String): AvailableCrop?

    fun findAllByUrbaniId(id: Long): Set<AvailableCrop>

    fun findByNameAndUrbaniId(name: String, id: Long): AvailableCrop?
}