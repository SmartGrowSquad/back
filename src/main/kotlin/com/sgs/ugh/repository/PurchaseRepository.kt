package com.sgs.ugh.repository

import com.sgs.ugh.entity.Purchase
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PurchaseRepository: JpaRepository<Purchase, Long> {
    fun findPurchasesByMemberId(id: Long) : Set<Purchase>
}