package com.sgs.ugh.controller.request

data class SaveAvailableCropRequest(
    val name: String,
    val price: Double,
    val description: String,
    val amount: Int,
    val urbaniId: Long,
)