package com.sgs.ugh.dto

import com.sgs.ugh.entity.Purchase

data class MemberDto(
    val id: Long,
    val name: String,
    val email: String,
    val address: String,
    val purchase: MutableSet<Purchase>
)
