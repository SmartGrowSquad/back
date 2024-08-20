package com.sgs.ugh.controller.request

data class UpdateCropAmountRequest(
    val id: Long,
    val amount: Int,
    val code: Boolean,
)