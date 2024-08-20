package com.sgs.ugh.controller.response

import com.sgs.ugh.entity.Purchase

data class GetPurchaseResponse(
    val history: Set<Purchase>
)
