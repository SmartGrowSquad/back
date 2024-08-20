package com.sgs.ugh.controller.request

data class PurchaseRequest (
    val acId: Long,
    val amount: Int,
    val memberId: Long,
)