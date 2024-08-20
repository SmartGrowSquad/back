package com.sgs.ugh.controller.request

data class UpdatePurchaseStateRequest (
    val id: Long,
    val urbaniId: Long,
    val status: Int,
)